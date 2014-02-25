$(function() {
	createTree();
});

function createTree() {
	$('#lizhitree')
			.treegrid(
					{
						title : 'SVN Repository',
						nowrap : false,
						rownumbers : true,
						collapsible : false,
						url : 'svnlog/repository.do?uri=',
						idField : 'id',
						treeField : 'relativeUrl',
						frozenColumns : [ [ {
							title : '地址',
							field : 'relativeUrl',
							width : 300,
							formatter : function(value) {
								var returnValue="";
								if(value.indexOf("/")>0){
									returnValue = value.substr(value.lastIndexOf("/"));
								}else{
									returnValue ="/"+value;
								}
								return '<span style="color:red">'
										+ decodeURI(returnValue) + '</span>';
							}

						} ] ],

						columns : [ [
								{
									field : 'fileName',
									title : '文件名称',
									width : 120
								},
								{
									field : 'latestSum',
									title : '现有代码(行)',
									width : 120,
									rowspan : 2

								},
								{
									field : 'originalSum',
									title : '原始代码(行)',
									width : 80,
									rowspan : 2

								},{
									field : 'Sum',
									title : '代码变动总量(行)',
									width : 80,
									rowspan : 2,
									formatter : function(value,row){
								          return row.modifySum+row.deleteSum+row.addSum;
								  }

								},{
									field : 'addSum',
									title : '添加代码(行)',
									width : 80,
									rowspan : 2

								},{
									field : 'deleteSum',
									title : '删除代码(行)',
									width : 80,
									rowspan : 2

								},{
									field : 'modifySum',
									title : '修改代码(行)',
									width : 80,
									rowspan : 2

								},
//								{
//									field : 'revision',
//									title : '版本号',
//									width : 80,
//									rowspan : 2
//								},
								{
									field : 'kind',
									title : '操作',
									width : 120,
									align : 'center',
									rowspan : 2,
									formatter : function(value) {
										return value == 'file' ? '<a onclick="displayAA()" style="cursor: pointer;color:red">变迁DIFF</a><a onclick="viewHistory()" style="margin-left:5px; cursor: pointer;color:red">日志记录</a>'
												: '';
									}

								} ] ],
						onBeforeExpand : function(row, param) {
							$(this).treegrid('options').url = 'svnlog/repository.do?uri='
									+ encodeURI(decodeURI(row.relativeUrl));
						},
					});
}

   function displayAA() {
	   
	 
		  setTimeout(function() {
			  var node = $('#lizhitree').treegrid('getSelected');
		    $('#showDiffData').datagrid({
		            url:'svnlog/showDiff.do',
		            queryParams:{
		            	id:node.id,
		            	repositoryRoot:node.repositoryRoot,
		            	relativeUrl:node.relativeUrl
		            },
		            columns:[[
		            {
		             field:'revisonM',
		             title:'对比版本号',
		             align :'center',
		             width:100,
		             formatter: function(value,row){
		            	 return value+" vs "+ row.revisonN;
		             }
		            },
		            {
		             field:'content',
		             title:'变更内容',
		             align :'center',
		             width:600,
		             height: 300
		             },
		            ]]
		            });
			  $('#dlg').dialog('open');
		      }, 200);
		}
//$(function(){
//	$("#showDiffdialog").dialog({
//		autoOpen : false,
//		height : 700
//	});
//});


$(function() {
	var name = $("#name"), url = $("#url"), svnUsername = $("#svnUsername"), allFields = $(
			[]).add(name).add(url).add(svnUsername), tips = $(".validateTips");

	function updateTips(t) {
		tips.text(t).addClass("ui-state-highlight");
		setTimeout(function() {
			tips.removeClass("ui-state-highlight", 1500);
		}, 500);
	}

	function checkLength(o, n, min, max) {
		if (o.val().length > max || o.val().length < min) {
			o.addClass("ui-state-error");
			updateTips(o.attr("title") + " 的长度为  " + min + " 和  "
					+ max + " 之间.");
			return false;
		} else {
			return true;
		}
	}

	function checkRegexp(o, regexp, n) {
		if (!(regexp.test(o.val()))) {
			o.addClass("ui-state-error");
			updateTips(n);
			return false;
		} else {
			return true;
		}
	}

	$("#dialog-form").dialog(
					{
						autoOpen : false,
						height : 400,
						width : 350,
						modal : true,
						buttons : {
							"添加" : function() {
								$('.datagrid-mask-msg').show();
								var bValid = true;
								allFields.removeClass("ui-state-error");
								bValid = bValid && checkLength(name, "name", 3, 16);
								bValid = bValid && checkLength(url, "url", 6, 500);
								bValid = bValid && checkLength(svnUsername, "svnUsername", 1,16);
                                var params = getQueryParams("createProjectFrom");
								if (bValid) {
									$.ajax({
										url:'svnlog/createProject.do',
										dataType : 'json',
										type : 'post',
//										processData : false,
										data : params,
										success: function(result) {
											if(result.ok){
												$("#dialog-modal").dialog({
													height: 140,
													modal: true
												});
												 $('.datagrid-mask-msg').hide();
												 $("#dialog-modal").text("添加成功");
											}
											$('#selectProject').combobox('reload');
										  }
									});
									$(this).dialog("close");
								}
							},
							"取消" : function() {
								$(this).dialog("close");
							}
						},
						close : function() {
							allFields.val("").removeClass("ui-state-error");
						}
					});

	$("#create-project").button().click(function() {
		$("#dialog-form").dialog("open");
	});
});

function getQueryParams(queryForm) {
	  var searchCondition = getJqueryObjById(queryForm).serialize();
	  var obj = {};
	  var pairs = searchCondition.split('&');
	  var name, value;
	  $.each(pairs, function(i, pair) {
	        pair = pair.split('=');
	        name = decodeURIComponent(pair[0]);
	        value = decodeURIComponent(pair[1]);
	        obj[name] = !obj[name] ? value : [].concat(obj[name]).concat(value); //若有多个同名称的参数，则拼接  
	      });
	  return obj;
	}

	/** 
	 * 根据id获取jquery对象 
	 * @param id 
	 */
	function getJqueryObjById(id) {
	  return $("#" + id);
	}


