Ext.Loader.setConfig({
	enabled : true
});
Ext.Loader.setPath('Ext.ux', 'js/ux');

Ext.require([ '*', 'Ext.ux.DataTip' ]);
function edittoken() {

	Ext.QuickTips.init();
	var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
	var json;
	Ext.Ajax.request({
		url : 'token?action=getconsumer',
		method : 'get',
		async : false,
		success : function(response, options) {
			json = response.responseText;
			json = Ext.decode(json);// 将json字符串转换为对象
		},
		failure : function() {
		}
	});
	var simple = Ext.widget({
		xtype : 'form',
		layout : 'form',
		id : 'simpleForm',
		renderTo : task,
		frame : true,
		title : document.title,
		bodyPadding : '5 5 0',
		width : 550,
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 125
		},
		plugins : {
			ptype : 'datatip'
		},
		defaultType : 'textfield',
		items : [ {
			fieldLabel : 'consumerkey',
			name : 'consumerkey',
			afterLabelTextTpl : required,
			allowBlank : false,
			disabled : true,
			value : json.consumerkey,
		// tooltip : 'consumerkey'
		}, {
			fieldLabel : 'consumersecret',
			name : 'consumersecret',
			disabled : true,
			// readOnly: true,
			value : json.consumersecret,
		// tooltip : "consumersecret"
		}, {
			fieldLabel : '是否使用代理',
			name : 'isproxy',
			value : json.isproxy,
		// tooltip : "二级分类，data文件中的c2字段"
		}, {
			fieldLabel : '代理ip',
			name : 'proxyhost',
			value : json.proxyhost,
		// tooltip : "二级分类，data文件中的c2字段"
		}, {
			fieldLabel : '代理端口',
			name : 'proxyport',
			value : json.proxyport,
		// tooltip : "二级分类，data文件中的c2字段"
		} ],

		buttons : [ {
			text : 'Save',
			handler : function() {
				var isvalid = this.up('form').getForm().isValid();
				if (isvalid) {
					this.up('form').getForm().submit({
						url : 'token?action=edit',
						submitEmptyText : false,
						success : function(form, action) {
							location.href = 'index.html';
						},
						waitMsg : 'Saving Data...'
					});
				}
			}
		}, {
			text : '返回',
			handler : function() {
				location.href = 'index.html';
			}
		} ]
	});
};
/* simple.render(document.body); */
Ext.onReady(edittoken);
