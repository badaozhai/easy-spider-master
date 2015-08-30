Ext.Loader.setConfig({
	enabled : true
});
Ext.Loader.setPath('Ext.ux', 'js/ux');

Ext.require([ '*', 'Ext.ux.DataTip' ]);
function showsearchwordtask() {

	Ext.QuickTips.init();
	var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
	var json;
	Ext.Ajax.request({
		url : 'showwordtask',
		method : 'get',
		async : false,
		success : function(response, options) {
			json = response.responseText;
			json = Ext.decode(json);//将json字符串转换为对象
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
			fieldLabel : '索引库名称',
			name : 'dbname',
			afterLabelTextTpl : required,
			allowBlank : false,
			value : json.dbname,
			tooltip : '索引库名称'
		}, {
			fieldLabel : '一级分类',
			name : 'c1',
			value : json.c1 ,
			tooltip : "一级分类，data文件中的c1字段"
		}, {
			fieldLabel : '二级分类',
			name : 'c2',
			value : json.c2 ,
			tooltip : "二级分类，data文件中的c2字段"
		}, {
			fieldLabel : '三级分类',
			name : 'c3',
			value : json.c3 ,
			tooltip : "三级分类，data文件中的c3字段"
		}, {
			fieldLabel : '四级分类',
			name : 'c4',
			value : json.c4 ,
			tooltip : "四级分类，data文件中的c4字段"
		}, {
			fieldLabel : '五级分类',
			name : 'c5',
			value : json.c5 ,
			tooltip : "五级分类，data文件中的c5字段"
		}, {
			fieldLabel : '六级分类',
			name : 'c6',
			value : json.c6 ,
			tooltip : "六级分类，data文件中的c6字段"
		}, {
			fieldLabel : '七级分类',
			name : 'c7',
			value : json.c7 ,
			tooltip : "七级分类，data文件中的c7字段"
		}, {
			fieldLabel : '八级分类',
			name : 'c8',
			value : json.c8 ,
			tooltip : "8级分类，data文件中的c8字段"
		}, {
			fieldLabel : '九级分类',
			name : 'c9',
			value : json.c9 ,
			tooltip : "九级分类，data文件中的c9字段"
		}, {
			fieldLabel : '十级分类',
			name : 'c10',
			value : json.c10 ,
			tooltip : "十级分类，data文件中的c10字段"
		}, {
			fieldLabel : '定时时间',
			name : 'quartz',
			value : json.quartz ,
			afterLabelTextTpl : required,
			allowBlank : false,
			tooltip : "任务的启动时间，使用quartz表达式，例如：(0 0 0/1 * * ?) 每小时更新一次"
		}, {
			fieldLabel : '采集页数',
			name : 'endpage',
			value : json.endpage ,
			afterLabelTextTpl : required,
			allowBlank : false,
			tooltip : "搜索的时候采集多少页，默认1页（每页100条）"
		}, {
			fieldLabel : '拷贝路径',
			name : 'copypath',
			value : json.copypath ,
			afterLabelTextTpl : required,
			allowBlank : false,
			tooltip : "data文件生成路径，将生成的数据拷贝到入库程序的目录下，如：C:\\import2db\\data\\192.168.0.1"
		}, {
			xtype : 'htmleditor',
			enableColors : false,
			enableFont : false,
			enableFontSize : false,
			enableLinks : false,
			enableLists : false,
			enableSourceEdit : false,
			enableFormat : false,
			disabled : false,
			afterLabelTextTpl : required,
			allowBlank : false,
			name : 'word',
			value : json.word ,
			enableAlignments : false,
			listeners : {
				afterrender : function(editor) {
					setTimeout(function() {
					}, 200);
				}
			}
		} ],

		buttons : [ {
			text : 'Save',
			handler : function() {
				var isvalid = this.up('form').getForm().isValid();
				if (isvalid) {
					this.up('form').getForm().submit({
						url : 'addword',
						submitEmptyText : false,
						success : function(form, action) {        
							location.href='index.html';
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
		} ]
	});
};
/* simple.render(document.body); */
Ext.onReady(showsearchwordtask);
