<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>微博采集-首页</title>
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
<style type="text/css">
input.val {
	height:26px;
line-height:22px;/*针对IE*/
width:68px;
}
</style>
</head>
<body>
	<form action="/spider.do?method=add" method="post">
	<div style="width:400px;height:300px;border:1px solid red;text-align:center;padding-top:20px;">
		 mainURL <input	name="mainURL" class="val"/><br> 
		 websiteName <input name="websiteName" class="val"/><br>
		 depth <input name="depth" value="1" class="val" /><br> 
		 threadNum <input name="threadNum" value="1" class="val" /><br> 
		 isDeleted <input	name="isDeleted" class="val"/><br> 
		 quartzParam <input name="quartzParam" value="0/10 0/1 * * * ?" class="val" /><br> 
		 <input type="submit" value="登陆">
	</div>
	</form>
</body>
</html>