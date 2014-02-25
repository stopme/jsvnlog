$(function() {
      createTree();
    });

function createTree() {
  $('#test').treegrid({
    title : 'SVN列表',
    nowrap : false,
    rownumbers : true,
    collapsible : false,
    url : 'projectmap?pid=',
    idField : 'url',
    treeField : 'url',
    frozenColumns : [[{
          title : '地址',
          field : 'url',
          width : 500,
          formatter : function(value) {
            return '<span style="color:red">' + decodeURI(value.substr(value.lastIndexOf("/"))) + '</span>';
          }
          
        }]],
    
    columns : [[{
          field : 'name',
          title : '名称',
          width : 120
        }, {
          field : 'size',
          title : '现有代码(行)',
          width : 120,
          rowspan : 2
          
        },{
          field : 'originalSum',
          title : '原始代码(行)',
          width : 80,
          rowspan : 2
          
        },{
          field : 'Sum',
          title : '变动总量(行)',
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
          
        }, {
          field : 'deleteSum',
          title : '减少代码(行)',
          width : 80,
          rowspan : 2
        }, {
          field : 'modifySum',
          title : '修改代码(行)',
          width : 80,
          rowspan : 2
        }, {
          field : 'revision',
          title : '版本号',
          width : 80,
          rowspan : 2
        }, {
          field : 'author',
          title : '作者',
          width : 100,
          rowspan : 2
        }, {
          field : 'date',
          title : '修改日期',
          width : 130,
          rowspan : 2,
          formatter : function(value) {
            var now = new Date(value);
            return now;
          }
        }, {
          field : 'commitMessage',
          title : '注释',
          width : 150,
          rowspan : 2
        }, {
          field : 'kind',
          title : '操作',
          width : 120,
          align : 'center',
          rowspan : 2,
          formatter : function(value) {
            return value == 'file'
                ? '<a onclick="displayAA()" style="cursor: pointer;color:red">变迁记录</a><a onclick="viewHistory()" style="margin-left:5px; cursor: pointer;color:red">历史版本</a>'
                : '';
          }
          
        }]],
    
    onBeforeExpand : function(row, param) {
    	var params = getQueryParams("queryForm");
    	var sta = params.startTime;
    	var end = params.endTime;
      $(this).treegrid('options').url = 'projectmap?pid=' + encodeURI(decodeURI(row.url))+'&startTime='+sta+'&endTime='+end;
    },
   onLoadSuccess : function(row, data){
   	  $('.datagrid-mask-msg').hide();
   }
  });
}
function displayAA() {
  setTimeout(function() {
        var node = $('#test').treegrid('getSelected');
        if (node != null)
          window.open("diff?uri=" + encodeURI(decodeURI(node.url)), '_blank');
      }, 200);
}

function viewHistory() {
  setTimeout(function() {
        var node = $('#test').treegrid('getSelected');
        if (node != null) {
          window.open(rfPath + "/svnlog/tohistory?uri=" + encodeURI(decodeURI(node.url)), '_blank');
        }
      }, 200);
  
}

function queryTable() {
  var params = getQueryParams("queryForm");
  $('.datagrid-mask-msg').show();
  $.post(rfPath + '/svnlog/projectmap?pid=', params, function(data) {
        $('#test').treegrid('loadData', data);
         $('.datagrid-mask-msg').hide();
      }, 'json')
}

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
