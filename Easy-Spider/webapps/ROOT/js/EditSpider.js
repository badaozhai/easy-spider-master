Ext.Loader.setConfig({
	enabled : true
});
Ext.Loader.setPath('Ext.ux', 'js/ux');

Ext.require([ '*', 'Ext.ux.DataTip' ]);
function EditSpider() {

	Ext.QuickTips.init();
	var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
	var json;
	var simple = Ext.widget({
		xtype : 'form',
		layout : 'form',
		id : 'simpleForm',
		renderTo : task,
		frame : true,
		title : '任务属性',
		bodyPadding : '5 5 0',
		width : 350,
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 75
		},
		plugins : {
			ptype : 'datatip'
		},
		defaultType : 'textfield',
		items : [ {
			xtype : 'button',
			buttonAlign : 'center',
			text : '编辑twitter搜索用户任务',
			listeners:{//添加监听事件 可以结合handler测试这两个事件哪个最先执行
                "click":function(){
                	location.href='UserSpider.html?twitter';
                }
            }
		}, {
			xtype : 'button',
			buttonAlign : 'center',
			text : '编辑twitter搜索关键字任务',
			listeners:{//添加监听事件 可以结合handler测试这两个事件哪个最先执行
                "click":function(){
                	location.href='SearchWordTask.html?twitter';
                }
            }
		}, {
			xtype : 'button',
			buttonAlign : 'center',
			text : '添加twitter搜索的账号',
			listeners:{//添加监听事件 可以结合handler测试这两个事件哪个最先执行
				"click":function(){
					location.href='adduser.html?twitter';
				}
			}
		}, {
			xtype : 'button',
			buttonAlign : 'center',
			text : '查看twitter搜索的账号',
			listeners:{//添加监听事件 可以结合handler测试这两个事件哪个最先执行
				"click":function(){
					location.href='showuser.html?twitter';
				}
			}
		}, {
			xtype : 'button',
			buttonAlign : 'center',
			text : '添加微博搜索的账号',
			listeners:{//添加监听事件 可以结合handler测试这两个事件哪个最先执行
				"click":function(){
					location.href='adduser.html?weibo';
				}
			}
		}, {
			xtype : 'button',
			buttonAlign : 'center',
			text : '查看微博搜索的账号',
			listeners:{//添加监听事件 可以结合handler测试这两个事件哪个最先执行
				"click":function(){
					location.href='showuser.html?weibo';
				}
			}
		}, {
			xtype : 'button',
			buttonAlign : 'center',
			text : '编辑token',
			listeners:{//添加监听事件 可以结合handler测试这两个事件哪个最先执行
				"click":function(){
					location.href='EditToken.html';
				}
			}
		} ],
	});
};
/* simple.render(document.body); */
Ext.onReady(EditSpider);
