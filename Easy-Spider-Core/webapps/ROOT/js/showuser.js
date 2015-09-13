Ext.Loader.setConfig({
	enabled : true
});
Ext.Loader.setPath('Ext.ux', 'js/ux');

Ext.require([ '*', 'Ext.ux.DataTip' ]);
function showuser() {

	Ext.QuickTips.init();
	var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';

	var simple = Ext.widget({
		xtype : 'form',
		layout : 'form',
		id : 'simpleForm',
		renderTo : task,
		frame : true,
		title : document.title,
		bodyPadding : '5 5 0',
		width : 850,
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 75
		},
		plugins : {
			ptype : 'datatip'
		},
		defaultType : 'textfield',
		items : [ {
			xtype : grid
		} ],

		buttons : [ {
			text : '返回',
			handler : function() {
				location.href = 'index.html';
			}
		} ]
	});
};
/* simple.render(document.body); */
Ext.onReady(showuser);
