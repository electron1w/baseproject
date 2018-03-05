$(function () {
    $('#tree1').ace_tree({
        dataSource: treeDataSource,
        multiSelect: false,
        loadingHTML: '<div class="tree-loading" ><i class="icon-refresh icon-spin blue"></i></div>',
        'open-icon': 'icon-minus',
        'close-icon': 'icon-plus',
        'selectable': true,
        'selected-icon': 'icon-ok',
        'unselected-icon': 'icon-remove'
    });
    $(".tree-folder-header").click(function () {
        menuInfo($(this).text());
    });
})

function treeLoad() {
    $('#tree1').ace_tree({
        dataSource: treeDataSource,
        multiSelect: false,
        loadingHTML: '<div class="tree-loading" ><i class="icon-refresh icon-spin blue"></i></div>',
        'open-icon': 'icon-minus',
        'close-icon': 'icon-plus',
        'selectable': true,
        'selected-icon': 'icon-ok',
        'unselected-icon': 'icon-remove'
    });
    $(".tree-folder-header").click(function () {
        menuInfo($(this).text());
    });

}

function menuInfo(text) {
    $.ajax({
        url: "menu/queryByName",
        cache: false,
        type: "post",
        data: {"name": text.trim()},
        success: function (result) {
            $("#parentidShow").show();
            $("#availableShow").show();
            $("#resourcetypeShow").show();
            $("#addFirstMenu").show();
            $("#addFirstMenu").text("添加菜单");
            $("#updateMenu").text("修改菜单");
            $("#delMenu").text("删除菜单");
            $("#menuTitleText").text("菜单属性");
            $("#id").val(result[0].id);
            $("#name").val(result[0].name);
            $("#parentid").val(result[0].parentid);
            $("#Url").val(result[0].url);
            $("#permission").val(result[0].permission);
            $("#form-field-select-1").val(result[0].resourcetype);
            $("#sort").val(result[0].sort);
            $("#ico").val(result[0].ico);
            $("#icoShow").attr("class",result[0].ico);
            var str="";
            for(var i=1;i<result.length;i++){
                str+="<button class=\"btn btn-inverse\" onclick=\"buttonDataShow("+result[i].id+")\">"+result[i].name+"</button>  ";
            }
            $("#buttonShow").html(str);
            if(str.length>0){
                $("#buttonZone").show();
            }else{
                $("#buttonZone").hide();
            }
            if (result[0].available) {
                $("#availableInput").html("  <input id=\"available\" name=\"available\"\n" +
                    " onclick=\"if($(this).val()==0){$(this).val('1');}else{$(this).val('0')}\"   value=\"1\"   checked='checked'  class=\"ace ace-switch ace-switch-7\" type=\"checkbox\"/>\n" +
                    "                                            <span class=\"lbl\"></span>");
            } else {
                $("#availableInput").html("  <input id=\"available\" name=\"available\"\n " +
                    "  onclick=\"if($(this).val()==0){$(this).val('1');}else{$(this).val('0')}\"   value=\"0\"    class=\"ace ace-switch ace-switch-7\" type=\"checkbox\"/>\n" +
                    "                                            <span class=\"lbl\"></span>");
            }
            $("#updateMenu").attr('style', '');
            $("#delMenu").attr('style', '');
        }, error: function () {
            alert("温馨提示", "请求错误!");
        }
    });
    getMenuSelect(0);
}
function buttonDataShow(id){
    $.ajax({
        url: "menu/queryById",
        cache: false,
        type: "post",
        data: {"id": id},
        success: function (result) {
            var str="";
            for (var i = 1; i < result.length; i++) {
                str+="<option value=\""+result[i].id+"\" >"+result[i].name+"</option>"
            }
            $("#parentid").html(str);
            $("#id").val(result[0].id);
            $("#name").val(result[0].name);
            $("#parentid").val(result[0].parentid);
            $("#Url").val(result[0].url);
            $("#permission").val(result[0].permission);
            $("#form-field-select-1").val(result[0].resourcetype);
            $("#sort").val(result[0].sort);
            $("#availableShow").hide();
            $("#resourcetypeShow").hide();
            $("#addFirstMenu").hide();
            $("#updateMenu").text("修改按钮");
            $("#delMenu").text("删除按钮");
            $("#menuTitleText").text("按钮属性");
            $("#updateMenu").attr('style', '');
            $("#delMenu").attr('style', '');
        }, error: function () {
            alert("温馨提示", "请求错误!");
        }
    });

}

function menuStyle(){
    $("#addFirstMenu").text("添加菜单");
    $("#updateMenu").text("修改菜单");
    $("#delMenu").text("删除菜单");
    $("#menuTitleText").text("菜单属性");
    $("#availableShow").show();
    $("#updateMenu").show();
    $("#delMenu").show();
    getMenuSelect(0);
}
function getMenuSelect(type){
    $.ajax({
        url: "menu/getMenuSelect?type="+type,
        cache: false,
        type: "post",
        success: function (data) {
            var str="";
            if(type==0){
                str="<option value=\"0\" >一级菜单</option>";
            }
            for (var i = 0; i < data.permissionList.length; i++) {
                str+="<option value=\""+data.permissionList[i].id+"\" >"+data.permissionList[i].name+"</option>"
            }
            $("#parentid").html(str);
        }, error: function () {
            alert("温馨提示", "请求错误!");
        }
    });
}
function buttonStyle(){
    $("#addFirstMenu").text("添加按钮");
    $("#delMenu").text("删除按钮");
    $("#menuTitleText").text("按钮属性");
    $("#availableShow").hide();
    $("#updateMenu").hide();
    $("#delMenu").hide();
    getMenuSelect(1);
}
function submitForm() {
    $.messager.confirm("操作提示", "您确定要执行操作吗？", function () {
        $.ajax({
            url: "menu/saveMenu",
            cache: false,
            dataType: 'json',
            data: $('#menuForm').serialize(),
            type: 'post',
            beforeSend: function () {
                // 禁用按钮防止重复提交
                $('#addFirstMenu').attr({disabled: "disabled"});
                $('#updateMenu').attr({disabled: "disabled"});
            },
            success: function (result) {
                if (result.flag == true) {
                    $.messager.alert('温馨提示', result.message);
                    $("#tree1").removeData("tree");
                    //清空事件
                    $("#tree1").unbind('click');
                    treeDataSource = new DataSourceTree({data: getTreeData()});
                    treeLoad();
                } else {
                    $.messager.alert('温馨提示', result.message);
                }
            },
            complete: function () {
                $('#addFirstMenu').removeAttr("disabled");
                $('#updateMenu').removeAttr("disabled");
            },
            error: function (data) {
                console.info("error: " + data.responseText);
            }
        });
    });
}

function firstButton() {
    if (checkInfo()) {
        $("#id").val("");
        $("#checkType").val("0");
        submitForm();
    }
}

function checkInfo() {
    if ($("#name").val().trim().length <= 0) {
        $.messager.alert('温馨提示', "请输入名称!");
        return false;
    }
    return true;
}


function updateButton() {
    if (checkInfoUpdate()) {
        $("#checkType").val("1");
        submitForm();
    }
}

function delButton() {
    if (checkInfoUpdate()) {
        $("#checkType").val("2");
        submitForm();
    }
}

function checkInfoUpdate() {
    if ($("#id").val().trim().length <= 0) {
        $.messager.alert('温馨提示', "获取基础信息异常,请点击修改菜单!");
        return false;
    }
    if ($("#parentid").val().trim().length <= 0) {
        $.messager.alert('温馨提示', "获取基础信息异常,请点击修改菜单!");
        return false;
    }
    if ($("#name").val().trim().length <= 0) {
        $.messager.alert('温馨提示', "请输入名称!");
        return false;
    }
    return true;
}