var userListVue = new Vue({
    el: '#userListDiv',
    data: {
        realName: undefined,
        sex: undefined,
        locked: undefined,
        pageNumber: undefined,
        pageSize: undefined
    },
    mounted: function () {//加载HTML页面渲染之后自动执行
        //使用mounted时，组件还在挂载还在new vue所以vue对象还没拿到 所以这里必须使用this.$data来获取vue里的data
    },
    methods: {
        getParams: function (params) {//获得table中的参数
            this.pageSize = params.limit;
            this.pageNumber = params.offset / params.limit + 1;//当前页开始的条数id/每页显示条数=当前页  后端是从1开始 前端从0开始所以加1
        },
        getUserList: function (request) {//此处request为bootstrapTable请求的参数
            axios.post('../user/selectUserList', this.$data).then(function (response) {
                request.success({//成功回调赋值
                    rows: response.data.list,
                    total: response.data.total
                });
                $('#table').bootstrapTable('load', response.data);//渲染表格数据
            })
        },
        refreshTable: function () {//搜索，删除，增加后执行
            $("#table").bootstrapTable("refresh");
        },
        formatSex: function (value, row, index) {
            return value === 1 ? "男" : "女";
        },
        formatLocked: function (value, row, index) {
            return value === 1 ? "正常" : "封禁";
        },
        updateUser: function () {
            layer.open({
                type: 2,
                title: '用户授权',
                shadeClose: true,
                shade: 0.5,
                skin: 'layui-layer-rim',
                closeBtn: 1,
                area: ['700px', '474px'],
                content: 'user_tail.html'
            });
        },
        addUser: function () {
            layer.open({
                type: 2,
                title: '添加用户',
                shadeClose: true,
                shade: 0.5,
                skin: 'layui-layer-rim',
                closeBtn: 1,
                area: ['700px', '474px'],
                content: 'user_tail.html'
            });
        }
    }
});