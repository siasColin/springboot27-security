var baseLink = $("#basepath").val();

var setting = {
    check: {
        enable: true,
        chkStyle: "radio",
        radioType: "all"
    },
    view: {
        dblClickExpand: false
    },
    data: {
        simpleData: {
            enable: true
        }
    },
    callback: {
        onCheck: function (e, treeId, treeNode){
            var parentId = treeNode.id;
            var parentName = treeNode.name;
            parent.document.getElementById('orgIdVal').value = parentId;
            parent.document.getElementById('orgName').value = parentName;
            $.remove()
        }
    }
};


$(document).ready(function() {
    initZTree();
});

function initZTree() {
    Common.ajax('sensitive/findOrgToTree', null, true, 'POST', treeDataCallBaxk);
}

function treeDataCallBaxk(data){

    var t = $.fn.zTree.init($("#userTree"), setting, data);
    var nodes = t.getNodes();
    for (var i = 0; i < nodes.length; i++) { //设置节点展开(二级节点)
        t.expandNode(nodes[i], true, false, true);
    }

    var _orgId = parent.document.getElementById('orgIdVal').value;
    if(_orgId){
        var node = t.getNodeByParam("id", _orgId);
        if(node!=null){
            t.checkNode(node, true, true);
            t.selectNode(node);
        }
    }
}

function zTreeOnclick(event, treeId, treeNode) {
    var treeObj=$.fn.zTree.getZTreeObj("userTree"),
        nodes=treeObj.getCheckedNodes(true),
        vId="",vName="",codes="";
    for(var i=0;i<nodes.length;i++){
        vId = vId == "" ? nodes[i].id : vId+","+nodes[i].id;
    }

    if (treeNode.id != null) {
        parent.document.getElementById('selectDisaterLevelIds').value=vId;
    }

}
