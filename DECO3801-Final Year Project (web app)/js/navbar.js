/**
 * display selected network in map
 *
 * @method populateNetworkList()
 */
function populateNetworkList() {
  if (networkSearch == null) var networkSearch = 'Network Names';
  if (filterSel == null) var filterSel = "net-filter";
  $.ajax({
    type: "POST",
    url: "map_search.php",
    data: {
      'input': "t",
      'search-type': networkSearch
    },
    success: function(data) {
      if (data == "") return;
      var list = JSON.parse(data);

      var select = $('#' + filterSel);

      for (var i = 0; i < list.length; i++) {
        select.append('<option value="' + list[i][0] + '">' +
          list[i][1] + '</option');
      }

      select.selectpicker('refresh');
    }
  });
}