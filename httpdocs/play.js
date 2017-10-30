function playPreviousSong(query) {
  var queryString = new String(query);
  var array = queryString.split("+");
  window.location.href = '\\play?playlist=' + array[0] + '&index=' + array[1];
}

function playNextSong(query) {
  var queryString = new String(query);
  var array = queryString.split("+");
  window.location.href = '\\play?playlist=' + array[0] + '&index=' + array[1];
}

function sharePrompt(text) {
  window.prompt("Share this link!: Ctrl+C the URL below", text);
}
