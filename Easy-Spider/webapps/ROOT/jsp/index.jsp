<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8""%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>微博采集-首页</title>
<link href="ext/resources/css/ext-all.css" rel="stylesheet"
	type="text/css" />
<script src="ext/adapter/ext/ext-base.js" type="text/javascript"></script>
<script src="ext/ext-all.js" type="text/javascript"></script>
<script src="js/EditSpider.js" type="text/javascript"></script>
<script src="js/checkuser.js" type="text/javascript"></script>
<script type="text/javascript">
	function getBoolean(str) {
		var b = false;
		str = str.toLowerCase();
		if (str == '0' || str == false || str == 'false' || str == 'off') {
			b = false;
		} else {
			b = true;
		}
		return b;
	}
	//鼠标右键菜单
	Ext.onReady(function() {
		//禁止整个页面的右键
		Ext.getDoc().on("contextmenu", function(e) {
			e.stopEvent();
		});
	});
</script>
</head>
<body>
	<div id='task' align="center"></div>
</body>
</html>