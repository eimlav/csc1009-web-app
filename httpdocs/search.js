var advSearchParents = [["love","0"], ["joy","0"], ["surprise","0"], ["anger","0"], ["sadness","0"], ["fear","0"]];
var advSearchChildren = [["affection","0"],["lust-longing","0"],["cheerfulness","0"],["contentment","0"],["pride","0"],["optimism","0"],
["enthrallment","0"],["relief","0"],["surprise","0"],["irritation","0"],["exasperation","0"],["rage-disgust","0"],["envy","0"],
["torment","0"],["suffering","0"],["sadness","0"],["disappointment","0"],["shame","0"],["neglect","0"],["sympathy","0"],["loneliness", "0"],
["horror","0"],["nervousness","0"]];
var advSearchGenres = [["alternative","0","Alternative"],["blues","0","Blues"],["childrens-music","0","Children's Music"],["classical","0","Classical"],["comedy","0","Comedy"],["commercial","0","Commerical"],
["country","0","Country"],["dance","0","Dance"],["edm","0","EDM"],["disney","0","Disney"],["easy-listening","0","Easy Listening"],["electronic","0","Electronic"]
,["enka","0","Enka"],["french-pop","0","French Pop"],["german-folk","0","German Folk"],["german-pop","0","German Pop"],["fitness","0","Fitness"],["hip-hop-rap","0","Hip-Hop/Rap"],
["holiday","0","Holiday"],["indie-pop","0","Indie Pop"],["industrial","0","Industrial"],["inspirational","0","Inspirational"],["instrumental","0","Instrumental"],["j-pop","0","J-Pop"]
,["jazz","0","Jazz"],["k-pop","0","K-Pop"],["karaoke","0","Karaoke"],["latin","0","Latin"],["new-age","0","New Age"],["opera","0","Opera"]
,["pop","0","Pop"],["randb-soul","0","R&B/Soul"],["reggae","0","Reggae"],["rock","0","Rock"],["singer-songwriter","0","Singer/Songwriter"],["spoken-word","0","Spoken Word"],["soundtrack","0","Soundtrack"]
,["tex-mex","0","Tex-Mex"],["vocal","0","Vocal"],["world","0","world"]];

function advSearchClickCell(id) {
  for(i = 0; i < advSearchParents.length; i++) {
      if(String(id) === advSearchParents[i][0]) {
        if(advSearchParents[i][1] === "0") {
          advSearchCellSelected(id, i, 0);
        } else {
          advSearchCellDeselected(id, i, 0);
        }
        return null;
      }
    }

  for(i = 0; i < advSearchChildren.length; i++) {
      if(String(id) === advSearchChildren[i][0]) {
        if(advSearchChildren[i][1] === "0") {
          advSearchCellSelected(id, i, 1);
        } else {
          advSearchCellDeselected(id, i, 1);
        }
        return null;
      }
    }

	for(i = 0; i < advSearchGenres.length; i++) {
      if(String(id) === advSearchGenres[i][0]) {
        if(advSearchGenres[i][1] === "0") {
          advSearchCellSelected(id, i, 2);
        } else {
          advSearchCellDeselected(id, i, 2);
        }
        return null;
      }
    }
}

function advSearchCellSelected(id, index, elementType) {
  $("#"+id).css({
      'border-radius' : '15px',
      'color':'#5a7f87',
      'background-color':'#99bfc6'
  });

  if(elementType === 0) {
    advSearchParents[parseInt(index)][1] = "1";
  } else if(elementType === 1){
    advSearchChildren[parseInt(index)][1] = "1";
  } else {
	  advSearchGenres[parseInt(index)][1] = "1";
  }
}

function advSearchCellDeselected(id, index, elementType) {
  $("#"+id).css({
      'border-radius' : '15px',
      'color':'#8aafb6',
      'background-color':'#a5cad1'
  });

  if(elementType === 0) {
    advSearchParents[parseInt(index)][1] = "0";
  } else if(elementType === 1){
    advSearchChildren[parseInt(index)][1] = "0";
  } else {
	  advSearchGenres[parseInt(index)][1] = "0";
  }
}

function advSearchPrintSelectedCells() {
  var selectedCellIds = "Emotional categories selected: \n";
  for(i = 0; i < advSearchParents.length; i++) {
    if(advSearchParents[i][1] == "1") {
      selectedCellIds += advSearchParents[i][0] + "\n";
    }
  }

  for(i = 0; i < advSearchChildren.length; i++) {
    if(advSearchChildren[i][1] == "1") {
      selectedCellIds += advSearchChildren[i][0] + "\n";
    }
  }

  for(i = 0; i < advSearchGenres.length; i++) {
    if(advSearchGenres[i][1] == "1") {
      selectedCellIds += advSearchGenres[i][0] + "\n";
    }
  }

  alert(selectedCellIds);
}

function displayAdvSearch() {
  $("#adv-search-form").css({
      'display':'block'
  });
}

// Detects if 'Enter' key is clicked in the nav bar search input
window.onload = function(){
  document.getElementById('nav-bar-search-input').onkeypress = function(e) {
      var event = e || window.event;
      var charCode = event.which || event.keyCode;

      if(charCode == '13' ) {
        performStnSearch('nav-bar-search-input', 'finalStnQueryNav');
        document.getElementById('nav-bar-search').submit();
        return false;
      }
      return true;
  }
};

function performStnSearch(id, hiddenID) {
  var searchQuery = new String();
  var list = new Array();
  searchQuery = document.getElementById(id).value;
  list = getArrayFromSpacedString(searchQuery);
  document.getElementById(hiddenID).value = list
}

function performAdvSearch() {
  var primaryEmotions = new Array();
  var secondaryEmotions = new Array();
  var genres = new Array();
  var artists = new Array();
  var songKeyWords = new Array();

  for(i = 0; i < advSearchParents.length; i++) {
    if(advSearchParents[i][1] == "1") {
      primaryEmotions.push(advSearchParents[i][0]);
    }
  }

  for(i = 0; i < advSearchChildren.length; i++) {
    if(advSearchChildren[i][1] == "1") {
      secondaryEmotions.push(advSearchChildren[i][0]);
    }
  }

  for(i = 0; i < advSearchGenres.length; i++) {
    if(advSearchGenres[i][1] == "1") {
      genres.push(advSearchGenres[i][0]);
    }
  }

  var artistQuery = document.getElementById("inputArtists").value;
  artists = getArrayFromSeperatedString(artistQuery);
  // if(artistQuery.substring(0,1) === "\"") {
  //   var quoteIndex = new Array();
  //   for(i = 0; i < artistQuery.length; i++) {
  //     if(artistQuery.charAt(i) === "\"") {
  //       quoteIndex.push(i);
  //     }
  //   }
  //   if(quoteIndex.length % 2 === 0) {
  //     var artistCount = (quoteIndex.length) / 2;
  //     for(i = 0; i < artistCount * 2; i+=2) {
  //       var currentArtist = artistQuery.substring(quoteIndex[i]+1, quoteIndex[i+1]);
  //       artists.push(currentArtist)
  //     }
  //   } else {
  //     // Odd number of quotes -> Incorrect format
  //   }
  // } else {
  //   artists.push(String(artistQuery));
  // }

  var songKeyWordsQuery = document.getElementById("inputSongKeyWords").value;
  songKeyWords = getArrayFromSpacedString(songKeyWordsQuery);

  document.getElementById("finalSongKeyWords").value = songKeyWords;
  document.getElementById("finalArtists").value = artists;
  document.getElementById("finalPrimaryEmotions").value = primaryEmotions;
  document.getElementById("finalSecondaryEmotions").value = secondaryEmotions;
  document.getElementById("finalGenres").value = genres;

  //alert("Song Key Words: " + songKeyWords + "\n\n" + "Primary: " + primaryEmotions + "\n\n" + "Seconary: " + secondaryEmotions + "\n\n" + "Genres: " + genres + "\n\n" + "Artists: " + artists);

}

function getArrayFromSpacedString(query) {
  var list = new Array();
  var spaceIndex = new Array();
  spaceIndex.push(0);

  for(i = 0; i < query.length; i++) {
    if(query.charAt(i) ===  ' ' || query.charAt(i) === '\t') {
      spaceIndex.push(i);
    }
  }

  spaceIndex.push(query.length);

  for(i = 0; i < spaceIndex.length - 1; i++) {
    var currentItem = query.substring(spaceIndex[i], spaceIndex[i + 1]);
    list.push(currentItem);
  }

  for(i = list.length - 1; i >= 0; i--) {
    if(list[i] === ' ') {
       list.splice(i, 1);
    }
  }

  return list;
}

function getArrayFromSeperatedString(query) {
  var list = new Array();
  if(query.substring(0,1) === "\"") {
    var quoteIndex = new Array();
    for(i = 0; i < query.length; i++) {
      if(query.charAt(i) === "\"") {
        quoteIndex.push(i);
      }
    }
    if(quoteIndex.length % 2 === 0) {
      var itemCount = (quoteIndex.length) / 2;
      for(i = 0; i < itemCount * 2; i+=2) {
        var currentItem = query.substring(quoteIndex[i]+1, quoteIndex[i+1]);
        list.push(currentItem);
      }
    } else {
      // Odd number of quotes -> Incorrect format
    }
  } else {
    list.push(String(query));
  }
  return list;
}

function logout() {
  document.cookie = 'username=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
  document.cookie = 'password=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}
