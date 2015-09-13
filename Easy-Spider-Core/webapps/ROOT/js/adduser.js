Ext.Loader.setConfig({
	enabled : true
});
Ext.Loader.setPath('Ext.ux', 'js/ux');

Ext.require([ '*', 'Ext.ux.DataTip' ]);
function adduser() {

	var source = document.URL;
	var posturl='addtwitteruser';
	if(source.indexOf("weibo") >= 0){
		posturl='addweibouser';
		document.title='添加微博采集账号';	
	}
	console.log(posturl);
	
	Ext.QuickTips.init();
	var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
	var json;
	var simple = Ext.widget({
		xtype : 'form',
		layout : 'form',
		id : 'simpleForm',
		renderTo : task,
		frame : true,
		title : document.title,
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
			fieldLabel : '用户名',
			name : 'user',
			afterLabelTextTpl : required,
			allowBlank : false,
			value:'xuyiming@toptimetech.com',
			tooltip : '用户名'
		}, {
			fieldLabel : '密码',
			name : 'password',
			afterLabelTextTpl : required,
			value:'111222111',
			allowBlank : false,
			tooltip : "密码"
		} ],

		buttons : [ {
			text : 'Save',
			handler : function() {
				var isvalid = this.up('form').getForm().isValid();
				if (isvalid) {
					this.up('form').getForm().submit({
						url : posturl,
						submitEmptyText : false,
						success : function(form, action) { 
							json=action.response.responseText;
							json = Ext.decode(json);//将json字符串转换为对象
							json=json.url;
							console.log(json);
							location.href=json;
					    }, 
						waitMsg : 'Saving Data...'
					});
				}
			}
		},{
			text : '返回',
			handler : function() {
				location.href='index.html';
			}
		}]
	});
};

Ext.onReady(adduser);
