Parse.Cloud.define("sendPush", function(request, response) {
  var params = request.params;
  var user = request.user;
  var messageText = params.text;

  var pushQuery = new Parse.Query(Parse.Installation);
  pushQuery.equalTo("deviceType", "android");

  Parse.Push.send({
    where: pushQuery,
    data: {
      "alert": messageText
    }
  }, {success: function() {
    console.log("#### PUSH OK");
  }, error: function(error) {
    console.log("#### PUSH ERROR" + error.message);
  }, userMasterKey: true});

  response.success("success");
});
