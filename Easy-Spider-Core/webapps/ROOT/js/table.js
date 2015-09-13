	var grid;
	Ext.onReady(function() {
		var store = Ext.create('Ext.data.Store', {
			fields : [ "user", "token"],
			pageSize : 100, // 页容量5条数据
			proxy : {
				type : 'ajax',
				url : 'token',
				// url : '/Tools/106.ashx?method=getAllCatalog',
				reader : { // 这里的reader为数据存储组织的地方，下面的配置是为json格式的数据，例如：[{"total":50,"rows":[{"cataId":"3","b":"4"}]}]
					type : 'json', // 返回数据类型为json格式
					root : 'rows', // 数据
					totalProperty : 'total' // 数据总条数
				}
			},
			sorters : [ {
				// 排序字段。
				property : 'ordeId',
				// 排序类型，默认为 ASC
				direction : 'desc'
			} ],
			autoLoad : true
		// 即时加载数据
		});

		grid = Ext.create('Ext.grid.Panel', {
			store : store,
			height : 400,
			width : 850,
			columns : [ {
				text : '用户名',
				dataIndex : 'user',
				align : 'left',
				width : 400
			}, {
				text : 'token',
				dataIndex : 'token',
				width : 450
			} ],
		});

	});

