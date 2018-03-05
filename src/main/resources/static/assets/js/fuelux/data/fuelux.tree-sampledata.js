var DataSourceTree = function (options) {
    this._data = options.data;
    this._delay = options.delay;
}

DataSourceTree.prototype.data = function (options, callback) {
    var self = this;
    var $data = null;

    if (!("name" in options) && !("type" in options)) {
        $data = this._data;//the root tree
        callback({data: $data});
        return;
    }
    else if ("type" in options && options.type == "folder") {
        if ("additionalParameters" in options && "children" in options.additionalParameters)
            $data = options.additionalParameters.children;
        else $data = {}//no data
    }

    if ($data != null)//this setTimeout is only for mimicking some random delay
        setTimeout(function () {
            callback({data: $data});
        });
    //we have used static data here
    //but you can retrieve your data dynamically from a server using ajax call
    //checkout examples/treeview.html and examples/treeview.js for more info
};

/*var tree_data = {
    'system': {name: '系统管理', type: 'folder'},
    /!*'test': {name: '测试', type: 'folder'},
    'vehicles': {name: 'Vehicles', type: 'folder'},
    'rentals': {name: 'Rentals', type: 'folder'},
    'real-estate': {name: 'Real Estate', type: 'folder'},
    'pets': {name: 'Pets', type: 'folder'},
    'tickets': {name: 'Tickets', type: 'item'},
    'services': {name: 'Services', type: 'item'},
    'personals': {name: 'Personals', type: 'item'}*!/
}
tree_data["system"]["additionalParameters"] = {
    "children": {
        "user": {name: '用户管理', type: 'item'},
        "role": {name: "角色管理", type: 'item'},
        "menu": {name: "菜单管理", type: 'item'}
    }
}*/

/*tree_data['test']['additionalParameters'] = {
    'children': {
        'appliances': {name: 'Appliances', type: 'item'},
        'arts-crafts': {name: 'Arts & Crafts', type: 'item'},
        'clothing': {name: 'Clothing', type: 'item'},
        'computers': {name: 'Computers', type: 'item'},
        'jewelry': {name: 'Jewelry', type: 'item'},
        'office-business': {name: 'Office & Business', type: 'item'},
        'sports-fitness': {name: 'Sports & Fitness', type: 'item'}
    }
}
tree_data['vehicles']['additionalParameters'] = {
    'children': {
        'cars': {name: 'Cars', type: 'folder'},
        'motorcycles': {name: 'Motorcycles', type: 'item'},
        'boats': {name: 'Boats', type: 'item'}
    }
}
tree_data['vehicles']['additionalParameters']['children']['cars']['additionalParameters'] = {
    'children': {
        'classics': {name: 'Classics', type: 'item'},
        'convertibles': {name: 'Convertibles', type: 'item'},
        'coupes': {name: 'Coupes', type: 'item'},
        'hatchbacks': {name: 'Hatchbacks', type: 'item'},
        'hybrids': {name: 'Hybrids', type: 'item'},
        'suvs': {name: 'SUVs', type: 'item'},
        'sedans': {name: 'Sedans', type: 'item'},
        'trucks': {name: 'Trucks', type: 'item'}
    }
}

tree_data['rentals']['additionalParameters'] = {
    'children': {
        'apartments-rentals': {name: 'Apartments', type: 'item'},
        'office-space-rentals': {name: 'Office Space', type: 'item'},
        'vacation-rentals': {name: 'Vacation Rentals', type: 'item'}
    }
}
tree_data['real-estate']['additionalParameters'] = {
    'children': {
        'apartments': {name: 'Apartments', type: 'item'},
        'villas': {name: 'Villas', type: 'item'},
        'plots': {name: 'Plots', type: 'item'}
    }
}
tree_data['pets']['additionalParameters'] = {
    'children': {
        'cats': {name: 'Cats', type: 'item'},
        'dogs': {name: 'Dogs', type: 'item'},
        'horses': {name: 'Horses', type: 'item'},
        'reptiles': {name: 'Reptiles', type: 'item'}
    }
}*/
function channelsToTreeData(chs) {
    var resultData = {};
    for (var i = 0; i < chs.permissionList.length; i++) {
        var pnode = {};
        var subchs = chs.permissionList[i].childPermission;
        pnode.name = chs.permissionList[i].name;
        pnode.id = chs.permissionList[i].id;
        if (subchs.length == 0) {
            pnode.type = "item";
        } else {
            pnode.type = "folder";
            var children = {};
            for (var j in subchs) {
                var lnode = {};
                lnode.name = subchs[j].name;
                lnode.type = "item";
                lnode.id = subchs[j].id;
                children[subchs[j].name] = lnode;
            }
            var additionalParameters = {};
            additionalParameters.children = children;
            pnode.additionalParameters = additionalParameters;
        }
        resultData[chs.permissionList[i].name] = pnode;
    }
    return resultData;
}

function getTreeData() {
    var resultData = {};
    $.ajax({
        url: "menu/queryMenu",
        type: 'post',
        async: false,
        dataType: 'json',
        success: function (data) {
            resultData = channelsToTreeData(data);
            var str="<option value=\"0\" >一级菜单</option>";
            for (var i = 0; i < data.permissionParent.length; i++) {
                str+="<option value=\""+data.permissionParent[i].id+"\" >"+data.permissionParent[i].name+"</option>"
            }
            $("#parentid").html(str);
        },
        error: function (response) {
            //console.log(response);
        }
    });
    console.log(resultData)
    return resultData;
}

var treeDataSource = new DataSourceTree({data: getTreeData()});

/*var tree_data_2 = {
    'pictures': {name: 'Pictures', type: 'folder', 'icon-class': 'red'},
    'music': {name: 'Music', type: 'folder', 'icon-class': 'orange'},
    'video': {name: 'Video', type: 'folder', 'icon-class': 'blue'},
    'documents': {name: 'Documents', type: 'folder', 'icon-class': 'green'},
    'backup': {name: 'Backup', type: 'folder'},
    'readme': {name: '<i class="icon-file-text grey"></i> ReadMe.txt', type: 'item'},
    'manual': {name: '<i class="icon-book blue"></i> Manual.html', type: 'item'}
}
tree_data_2['music']['additionalParameters'] = {
    'children': [
        {name: '<i class="icon-music blue"></i> song1.ogg', type: 'item'},
        {name: '<i class="icon-music blue"></i> song2.ogg', type: 'item'},
        {name: '<i class="icon-music blue"></i> song3.ogg', type: 'item'},
        {name: '<i class="icon-music blue"></i> song4.ogg', type: 'item'},
        {name: '<i class="icon-music blue"></i> song5.ogg', type: 'item'}
    ]
}
tree_data_2['video']['additionalParameters'] = {
    'children': [
        {name: '<i class="icon-film blue"></i> movie1.avi', type: 'item'},
        {name: '<i class="icon-film blue"></i> movie2.avi', type: 'item'},
        {name: '<i class="icon-film blue"></i> movie3.avi', type: 'item'},
        {name: '<i class="icon-film blue"></i> movie4.avi', type: 'item'},
        {name: '<i class="icon-film blue"></i> movie5.avi', type: 'item'}
    ]
}
tree_data_2['pictures']['additionalParameters'] = {
    'children': {
        'wallpapers': {name: 'Wallpapers', type: 'folder', 'icon-class': 'pink'},
        'camera': {name: 'Camera', type: 'folder', 'icon-class': 'pink'}
    }
}
tree_data_2['pictures']['additionalParameters']['children']['wallpapers']['additionalParameters'] = {
    'children': [
        {name: '<i class="icon-picture green"></i> wallpaper1.jpg', type: 'item'},
        {name: '<i class="icon-picture green"></i> wallpaper2.jpg', type: 'item'},
        {name: '<i class="icon-picture green"></i> wallpaper3.jpg', type: 'item'},
        {name: '<i class="icon-picture green"></i> wallpaper4.jpg', type: 'item'}
    ]
}
tree_data_2['pictures']['additionalParameters']['children']['camera']['additionalParameters'] = {
    'children': [
        {name: '<i class="icon-picture green"></i> photo1.jpg', type: 'item'},
        {name: '<i class="icon-picture green"></i> photo2.jpg', type: 'item'},
        {name: '<i class="icon-picture green"></i> photo3.jpg', type: 'item'},
        {name: '<i class="icon-picture green"></i> photo4.jpg', type: 'item'},
        {name: '<i class="icon-picture green"></i> photo5.jpg', type: 'item'},
        {name: '<i class="icon-picture green"></i> photo6.jpg', type: 'item'}
    ]
}


tree_data_2['documents']['additionalParameters'] = {
    'children': [
        {name: '<i class="icon-file-text red"></i> document1.pdf', type: 'item'},
        {name: '<i class="icon-file-text grey"></i> document2.doc', type: 'item'},
        {name: '<i class="icon-file-text grey"></i> document3.doc', type: 'item'},
        {name: '<i class="icon-file-text red"></i> document4.pdf', type: 'item'},
        {name: '<i class="icon-file-text grey"></i> document5.doc', type: 'item'}
    ]
}

tree_data_2['backup']['additionalParameters'] = {
    'children': [
        {name: '<i class="icon-archive brown"></i> backup1.zip', type: 'item'},
        {name: '<i class="icon-archive brown"></i> backup2.zip', type: 'item'},
        {name: '<i class="icon-archive brown"></i> backup3.zip', type: 'item'},
        {name: '<i class="icon-archive brown"></i> backup4.zip', type: 'item'}
    ]
}
var treeDataSource2 = new DataSourceTree({data: tree_data_2});*/
