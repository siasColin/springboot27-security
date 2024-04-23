var _header = $("meta[name='_csrf_header']").attr("content");
var _token = $("meta[name='_csrf']").attr("content");


var baseLink = $("#basepath").val();
var form;
$(function () {

    $("#chongzhi").click(function () {
        $("input").val("");
        $("#infoType").val("");
    });

    $("#orgName").on('click',function(){

        layer.open({
            type: 2,
            title:'选择机构',
            closeBtn: 1,
            scrollbar: false, //  滚动条 禁止
            btn: ['确定'],
            area: ['350px', '450px'], //宽高
            content: baseLink+'sensitive/toGetOrgZtreeList',
            yes: function(index, layero){
                layer.close(index); // 关闭当前对话框
            },
        });


        Common.ajax('sensitive/findOrgToTree', param, true, 'POST', ztreeCallBack);


    })

});

function ztreeCallBack(data){
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
                $("#orgId").val(parentId);
                $("#orgName").val(parentName);
                $.remove()
            }
        }
    };
    var treeObj = $.fn.zTree.init($("#orgModuleTree"), setting,data);
}

layui.use([ 'form','layer', 'table','upload'], function(){
    var layer = layui.layer //弹层
        ,table = layui.table //表格
        ,upload = layui.upload; //上传
    form = layui.form;



    // form.on('click(nameClick)', function(data){
    //     layer.msg(1111);
    // })

    //执行一个 table 实例
    table.render({
        id: 'psminfoReload'
        ,elem: '#psminfolist'
        ,height: 'full-80'
        ,limilt:20
        ,method:'get'
        ,url: Common.ctxPath +'sensitive/sensitiveList' //数据接口
        ,title: '其他重点单位信息'
        ,page: true //开启分页
        ,toolbar: '#info_toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
        ,defaultToolbar: ['filter', 'print']
        ,totalRow: false //开启合计行
        ,cols: [[ //表头
            {type: 'checkbox', fixed: 'left'}
            ,{type:'numbers', title: '序号'}
            ,{field: 'word', title: '敏感词' }
            ,{field: 'isshare', title: '是否共享'}
            ,{field: 'orgname', title: '机构名称'}
            ,{field: 'worddes', title: '敏感字词描述'}
            ,{field: 'createusername', title: '创建人员'}

            ,{fixed: 'right', width:165, align:'center', toolbar: '#barPsmInfolist'}
        ]]
    });

    var $ = layui.$, active = {
        reload: function(){
            var wordStr = $('#wordStr');
            var isShareStr = $('#isShareStr');
            var orgIdVal = $('#orgIdVal');
            //执行重载
            table.reload('psminfoReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    word: wordStr.val(),
                    isShare: isShareStr.val(),
                    orgId: orgIdVal.val()
                }
            });

        },
        downExcel: function(){
            var word = $('#wordStr').val();
            var isShare = $('#isShareStr').val();
            var orgIdVal = $('#orgIdVal').val();
            location.href=baseLink+'sensitive/downExcel?orgId='+orgIdVal+'&isShare='+isShare+"&word="+encodeURI(encodeURI(word));
            //location.href = baseLink + 'sensitive/downloadTemplate';
        }
    };
    $('.demoTable .layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    //监听头工具栏事件
    table.on('toolbar(test)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id)
            ,data = checkStatus.data; //获取选中的数据
        switch(obj.event){
            case 'add':
                let html = '';
                html += '<form class="layui-form warnFormBox" action="" style="color:#333 !important;padding: 10px;">';
                html += '	<div class="layui-form-item">';
                html += '		<div class="layui-inline">';
                html += '			<label class="layui-form-label" style="width: 95px;">敏感词：</label>';
                html += '			<div class="layui-input-inline" style="width: 315px !important;">';
                html += '				<input id="word" name="word" type="text" lay-verify="required" autocomplete="off" class="layui-input">';
                html += '			</div>';
                html += '		</div>';
                html += '		<div class="layui-inline">';
                html += '			<label class="layui-form-label" style="width: 95px;">是否共享：</label>';
                html += '			<div class="layui-input-inline">';
                html += '				<input type="radio" name="isShare" value="1" title="是">';
                html += '				<input type="radio" name="isShare" value="0" title="否" checked>';
                html += '			</div>';
                html += '		</div>';
                html += '		<div class="layui-inline">';
                html += '			<label class="layui-form-label" style="width: 95px;">敏感词描述：</label>';
                html += '			<div class="layui-input-inline">';
                html += '			<textarea id="wordDes" name="wordDes" style="width: 313px;"  placeholder="请输入内容" class="layui-textarea"></textarea>';
                html += '			</div>';
                html += '		</div>';
                html += '	</div>';
                html += '</form>';
                layer.open({
                    title:"敏感词增加",
                    type: 1,
                    area: ['500px','360px'],
                    btn: ['保存', '取消'],
                    content: html,
                    yes: function(index,layero){
                        var word = $("#word").val();
                        var wordDes = $("#wordDes").val();
                        var isShare = $("input[name='isShare']:checked").val();
                        var param = {};
                        param.isShare = isShare;
                        param.wordDes = wordDes;
                        param.word = word;
                        Common.ajax('sensitive/add', param, true, 'POST', addCallBack);

                        layer.close(index);
                        table.reload('psminfoReload', {
                            page: {
                                curr: 1 //重新从第 1 页开始
                            }
                        });
                    }
                });
                form.render();
                break;
            case 'upload': //文件上传
                uploadLayerIndex = layer.open({
                    type: 1
                    ,title: '导入'
                    ,area: ['300px', '150px']
                    ,content: $("#upload_choice")
                });
                break;
            case 'exportExcel':
                var word = $('#wordStr').val();
                var isShare = $('#isShareStr').val();
                var orgIdVal = $('#orgIdVal').val();
                location.href = baseLink+'sensitive/downExcel?orgId='+orgIdVal+'&isShare='+isShare+"&word="+encodeURI(encodeURI(word));
                break;
            case 'delete':
                if(data.length === 0){
                    layer.msg('请选择一行');
                } else {
                    layer.confirm('确定删除吗?', function(index){
                        var ids = "";
                        for(var i=0;i<data.length;i++){
                            ids += data[i].keyid+',';
                        }
                        var param = {};
                        param.keyId = ids.substring(0,ids.length-1);
                        Common.ajax('sensitive/delById', param, true, 'POST', addCallBack);


                    });
                }
                break;
            case 'upload': //文件上传
                uploadLayerIndex = layer.open({
                    type: 1
                    ,title: '导入'
                    ,area: ['300px', '150px']
                    ,content: $("#upload_choice")
                });
                break;
                $("#uploadExcel").click();
                break;
            case 'down':
                window.location.href=baseLink+"sensitive/downloadTemplate?fileName=otherinfo&showName="+encodeURI(encodeURI("敏感词导入模板"))+"&suffix=xlsx";
                break;
        };
    });

    //监听行工具事件
    table.on('tool(test)', function(obj){
        var data = obj.data //获得当前行数据
            ,layEvent = obj.event; //获得 lay-event 对应的值
        if(layEvent === 'detail'){
            let html = '';
            html += '<form class="layui-form warnFormBox" action="" style="color:#333 !important;padding: 10px;">';
            html += '	<div class="layui-form-item">';
            html += '		<div class="layui-inline">';
            html += '			<label class="layui-form-label" style="width: 95px;">敏感词：</label>';
            html += '			<div class="layui-input-inline" style="width: 315px !important;">';
            html += '				<input id="word" name="word" type="text" lay-verify="required" readonly value="'+data.word+'" autocomplete="off" class="layui-input">';
            html += '			</div>';
            html += '		</div>';
            html += '		<div class="layui-inline">';
            html += '			<label class="layui-form-label" style="width: 95px;">是否共享：</label>';
            html += '			<div class="layui-input-inline">';
            if(data.isshare == '是'){
                html += '				<input type="radio" name="isShare" value="1" disabled title="是" checked>';
                html += '				<input type="radio" name="isShare" value="0" disabled title="否">';
            }else{
                html += '				<input type="radio" name="isShare" value="1" disabled title="是">';
                html += '				<input type="radio" name="isShare" value="0" disabled title="否" checked>';
            }

            html += '			</div>';
            html += '		</div>';
            html += '		<div class="layui-inline">';
            html += '			<label class="layui-form-label" style="width: 95px;">敏感词描述：</label>';
            html += '			<div class="layui-input-inline">';
            html += '			<textarea id="wordDes" name="wordDes" style="width: 313px;" readonly  placeholder="请输入内容" class="layui-textarea">'+data.worddes+'</textarea>';
            html += '			</div>';
            html += '		</div>';
            html += '	</div>';
            html += '</form>';
            layer.open({
                title:"敏感词详情",
                type: 1,
                area: ['500px','360px'],
                btn: ['关闭'],
                content: html,
                success: function(layero, index){
                    // var iframe = window['layui-layer-iframe' + index];
                    // iframe.child(data.id,"detail");
                }
            });
            form.render();
        } else if(layEvent === 'del'){
            var orgId = $("#orgId").val();
            if(orgId != data.orgid){
                layer.msg("非本机构数据不能进行删除操作!!!");
                return false;
            }else{
                layer.confirm('确定删除吗?', function(index){
                    var param = {};
                    param.keyId = data.keyid;
                    Common.ajax('sensitive/delById', param, true, 'POST', addCallBack);

                });
            }
        } else if(layEvent === 'edit'){
            var orgId = $("#orgId").val();
            if(orgId != data.orgid){
                layer.msg("非本机构数据不能进行删除操作!!!");
                return false;
            }else{
                let html = '';
                html += '<form class="layui-form warnFormBox" action="" style="color:#333 !important;padding: 10px;">';
                html += '	<input type="hidden" name="keyId" id="keyId" value="'+data.keyid+'">';
                html += '	<div class="layui-form-item">';
                html += '		<div class="layui-inline">';
                html += '			<label class="layui-form-label" style="width: 95px;">敏感词：</label>';
                html += '			<div class="layui-input-inline" style="width: 315px !important;">';
                html += '				<input id="word" name="word" type="text" lay-verify="required" value="'+data.word+'" autocomplete="off" class="layui-input">';
                html += '			</div>';
                html += '		</div>';
                html += '		<div class="layui-inline">';
                html += '			<label class="layui-form-label" style="width: 95px;">是否共享：</label>';
                html += '			<div class="layui-input-inline">';
                if(data.isshare == '是'){
                    html += '				<input type="radio" name="isShare" value="1" title="是" checked>';
                    html += '				<input type="radio" name="isShare" value="0" title="否">';
                }else{
                    html += '				<input type="radio" name="isShare" value="1" title="是">';
                    html += '				<input type="radio" name="isShare" value="0" title="否" checked>';
                }

                html += '			</div>';
                html += '		</div>';
                html += '		<div class="layui-inline">';
                html += '			<label class="layui-form-label" style="width: 95px;">敏感词描述：</label>';
                html += '			<div class="layui-input-inline">';
                html += '			<textarea id="wordDes" name="wordDes" style="width: 313px;"  placeholder="请输入内容" class="layui-textarea">'+data.worddes+'</textarea>';
                html += '			</div>';
                html += '		</div>';
                html += '	</div>';
                html += '</form>';
                layer.open({
                    title:"敏感词信息",
                    type: 1,
                    area: ['500px','360px'],
                    btn: ['保存', '取消'],
                    content: html,
                    success: function(layero, index){
                        // var iframe = window['layui-layer-iframe' + index];
                        // iframe.child(data.id);
                    },
                    yes: function(index,layero){
                        // // 获取iframe层的body
                        // var body = layer.getChildFrame('body', index);
                        // // 找到隐藏的提交按钮模拟点击提交
                        // body.find('#permissionSubmit').click();
                        var param = {};
                        param.word = $("#word").val();
                        param.keyId = $("#keyId").val();
                        Common.ajax('sensitive/findIsExit', param, true, 'POST', exitCallBack);

                    }
                });
                form.render();
            }
        }
    });
    //上传
    var uploadInst = upload.render({
        elem: '.import'
        ,url: baseLink+'sensitive/excelUpload'
        //,method:'get'
        ,accept: 'file' //普通文件Ev
        ,before: function () {
            layer.load();
        }
        ,done: function(res){
            layer.closeAll('loading'); //关闭loading
            layer.close(uploadLayerIndex); //关闭弹出框
            if(res.code == 0){
                layer.msg(res.msg);
                var filepath = res.data.filepath;
                //location.href = baseLink + "common/downImportFile?filepath="+encodeURI(encodeURI(filepath));
                window.location.href = baseLink+"sensitive/downImportFile?filepath="+encodeURI(encodeURI(filepath));

                //$("#success").attr("src",baseLink+"common/downImportFile?filepath="+encodeURI(encodeURI(filepath)));

            }else{
                layer.msg(res.msg);
            }

            table.reload('psminfoReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
            });
        }
    });

    function exitCallBack(data){
        if(data.countnum > 0){
            layer.msg("该敏感词已经存在，请重新输入!!!");
            return false;
        }else{
            var param = {};
            param.word = $("#word").val();
            param.keyId = $("#keyId").val();
            param.wordDes = $("#wordDes").val();
            param.isShare = $("input[name='isShare']:checked").val();
            Common.ajax('sensitive/update', param, true, 'POST', addCallBack);
            layer.closeAll();
            table.reload('psminfoReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
            });
        }
    }

    function addCallBack(data){
        layer.msg("操作成功");
        table.reload('psminfoReload', {
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    }
});



