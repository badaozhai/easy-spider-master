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

var task_CheckLoginState;// 声明任务变量
Ext.onReady(function() {
	task_CheckLoginState = {
		run : checkLogin,// 执行任务时执行的函数
		interval : 60000
	// 任务间隔，毫秒为单位，这里是10秒
	}
	var source = document.URL;
	//如果来源地址当中包含
	if(source.indexOf("weibo") >= 0||source.indexOf("twitter") >= 0){
		Ext.TaskMgr.start(task_CheckLoginState);// 初始化时就启动任务
	}
});

// 检查登录状态的函数
function checkLogin() {
	var source = document.URL;
	var posturl='&source=twitter';
	if(source.indexOf("weibo") >= 0){
		posturl='&source=twitter';
		document.title='添加微博采集账号';	
	}
	Ext.Ajax.request({
		url : 'token?action=ask',
		method : 'get',
		async : false,
		success : function(response, options) {
			var json = response.responseText;
			if (!getBoolean(json)) {
				Ext.MessageBox.alert("系统信息", '采集器目前没有添加用户，采集任务不能运行', function(
						btnId) {
					if (getBoolean(btnId)) {
						window.open('adduser.html');
					}
				});
			}
		},
		failure : function() {
		}
	});
}