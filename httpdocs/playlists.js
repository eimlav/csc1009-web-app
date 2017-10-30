function createPlaylist() {
    var playlistName = document.getElementById("formCreateValue").value;
    document.getElementById("finalCreateName").value = playlistName;
}

function removePlaylist() {
    var selectElement = document.getElementById("formRemoveValue");
    var selectedPlaylistID = selectElement.options[selectElement.selectedIndex].value;

    document.getElementById("finalRemovePlaylistID").value = selectedPlaylistID;
}

function addSong() {
    var selectElement = document.getElementById("formAddValue");
    var selectedPlaylistID = selectElement.options[selectElement.selectedIndex].value;

    document.getElementById("finalAddPlaylistID").value = selectedPlaylistID;
}

function getCookieInfo() {
  var info = new Array();
  // Get cookie info
  // The following is only temp data
  info.push("eimhin01");
  return info;
}
