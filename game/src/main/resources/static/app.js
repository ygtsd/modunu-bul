let stompClient = null;
let currentRoomId = null;
let currentPlayerId = null;
let previousStatus = null;
let latestGameState = null;
let isCountingDown = false;

function createRoom() {
    fetch('/api/game/create', { method: 'POST' })
        .then(response => response.json())
        .then(data => {
            alert("Oda kuruldu! Oda Kodu: " + data.roomId);
            document.getElementById('room-code').value = data.roomId;
        });
}

function joinRoom() {
    currentRoomId = document.getElementById('room-code').value;
    currentPlayerId = document.getElementById('player-name').value;

    if(!currentRoomId || !currentPlayerId) {
        alert("Lütfen oda kodu ve isminizi girin.");
        return;
    }

    let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/game/' + currentRoomId, function (message) {
            let gameState = JSON.parse(message.body);
            updateUI(gameState);
        });

        stompClient.send("/app/game.join/" + currentRoomId, {}, currentPlayerId);

        document.getElementById('setup-screen').style.display = 'none';
        document.getElementById('game-screen').style.display = 'block';
    });
}

function playSelectedCard(cardImage) {
    let payload = {
        playerId: currentPlayerId,
        cardImage: cardImage
    };
    stompClient.send("/app/game.play/" + currentRoomId, {}, JSON.stringify(payload));
}

function voteForPlayer(votedPlayerId) {
    stompClient.send("/app/game.vote/" + currentRoomId, {}, votedPlayerId);
}

function updateUI(gameState) {
    latestGameState = gameState;

    if (previousStatus === "PLAYING" && gameState.status === "VOTING" && !isCountingDown) {
        triggerCountdown();
        previousStatus = gameState.status;
        return;
    }

    if (isCountingDown) {
        return;
    }

    previousStatus = gameState.status;
    renderGameScreen(gameState);
}

function triggerCountdown() {
    isCountingDown = true;
    let secondsLeft = 3;

    document.getElementById('hand-display').innerHTML = "<p>Geri sayım yapılıyor, lütfen bekleyin...</p>";

    const tableDisplay = document.getElementById('table-display');
    const countdownArea = document.getElementById('countdown-area');

    tableDisplay.innerHTML = "";
    countdownArea.innerHTML = `<div class="countdown-text">FOTOĞRAFLAR AÇILIYOR: ${secondsLeft}</div>`;

    let timer = setInterval(() => {
        secondsLeft--;
        if (secondsLeft > 0) {
            countdownArea.innerHTML = `<div class="countdown-text">FOTOĞRAFLAR AÇILIYOR: ${secondsLeft}</div>`;
        } else {
            clearInterval(timer);
            countdownArea.innerHTML = `<div class="countdown-text" style="color: #27ae60;">AÇILDI! 🎉</div>`;

            setTimeout(() => {
                countdownArea.innerHTML = "";
                isCountingDown = false;
                renderGameScreen(latestGameState);
            }, 1000);
        }
    }, 1000);
}

function renderGameScreen(gameState) {
    document.getElementById('room-display').innerText = "Oda: " + gameState.roomId + " | Tur: " + gameState.currentRound + " | Durum: " + gameState.status;

    if(gameState.currentSituation) {
        document.getElementById('situation-display').innerText = gameState.currentSituation;
    }

    let scoresHtml = "";
    let myHand = [];
    let hasIPlayed = false;

    gameState.players.forEach(p => {
        scoresHtml += `<p><strong>${p.playerId}:</strong> ${p.score} puan</p>`;

        if(p.playerId === currentPlayerId) {
            myHand = p.hand;
            hasIPlayed = p.hasPlayedThisRound;
        }
    });
    document.getElementById('scores-display').innerHTML = scoresHtml;

    let handHtml = "";
    if (gameState.status === "PLAYING" && !hasIPlayed) {
        myHand.forEach(card => {
            // Sınıfları "polaroid" olarak güncelledik
            handHtml += `<div class="polaroid" onclick="playSelectedCard('${card}')">
                            <img src="/images/${card}" class="polaroid-img" alt="Kart">
                         </div>`;
        });
    } else if (hasIPlayed) {
        handHtml = "<p>Fotoğrafını seçtin, diğerini bekliyorsun...</p>";
    } else {
        handHtml = "<p>Şu an kart atılamaz.</p>";
    }
    document.getElementById('hand-display').innerHTML = handHtml;

    let tableHtml = "";
    if (Object.keys(gameState.table).length > 0) {
        for (const [pId, card] of Object.entries(gameState.table)) {
            // Masadaki kartları da polaroid kutusuna aldık
            tableHtml += `<div class="polaroid" style="cursor:default; text-align:center;">`;

            if (gameState.status === "PLAYING") {
                tableHtml += `<div class="card-back">❓</div>`;
                tableHtml += `<p style="margin-top:10px;"><strong>${pId}</strong> attı</p>`;
            } else {
                tableHtml += `<img src="/images/${card}" class="polaroid-img-small" alt="Kart">`;
                tableHtml += `<p style="margin-top:5px; margin-bottom:0;"><strong>${pId}</strong></p>`;
                if (gameState.status === "VOTING") {
                    tableHtml += `<button class="vote-btn" onclick="voteForPlayer('${pId}')">Buna Oy Ver!</button>`;
                }
            }
            tableHtml += `</div>`;
        }
    } else {
        tableHtml = "Henüz kart atılmadı.";
    }
    document.getElementById('table-display').innerHTML = tableHtml;
}