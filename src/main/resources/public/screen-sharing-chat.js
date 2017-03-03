var screen_img_src = id("screen_img").src;
var userName = prompt("Enter Your name", "");
initChat();

function initChat() {
    initWebSocket();
    initMessageHandlers();
}

function initWebSocket() {
    webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat/");
    webSocket.onmessage = function (msg) { handleMessage(msg); };
    webSocket.onclose = function () { alert("WebSocket connection closed") };
}

function initMessageHandlers() {
    messageHandler = {
        hello: acquaintance,
        chat: updateChat,
        list: updateUserList,
        screen: updateScreen,
    };
}

function initInputControl(data) {
    var input = id("message");
    input.addEventListener("keypress", function (e) {
        if (e.keyCode === 13) { sendMessage(e.target.value); }
    });
    input.maxLength = data.msglen;
    input.placeholder = "Write a message... (Max length " + data.msglen + ")";
    input.focus();
}

function sendSetName(userName) {
    sendRequest({type: "setname", name: userName ? userName : ""});
}

function sendMessage(text) {
    if (text !== "") {
        sendRequest({type: "chat", message: text});
        id("message").value = "";
    }
}

function sendRequest(request) {
    webSocket.send(JSON.stringify(request));
}

function handleMessage(message) {
    var data = JSON.parse(message.data);
    var handler = messageHandler[data.type];
    if (undefined != handler)
        handler(data);
}

function acquaintance(data) {
    sendSetName(userName);
    initInputControl(data);
}

function updateChat(data) {
    insert("history", "<article>" + data.time
                    + " <b>" + data.user
                    + "</b><p>" + data.message
                    + "</p></article>");
    scrollToBottom(id("history"));
}

function updateUserList(data) {
    id("userlist").innerHTML = "";
    data.userlist.forEach(function (user) {
        insert("userlist", "<li>" + user + "</li>");
    });
}

function updateScreen(data) {
    id("screen_img").src = screen_img_src + "?" + new Date().getTime();
}

function scrollToBottom(element) {
    element.scrollTop = element.scrollHeight;
}

function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("beforeend", message);
}

function id(id) {
    return document.getElementById(id);
}
