var userListVue = new Vue({
    el: '#userListDiv',
    data: {
        realName: '',
        sex: undefined,
        locked: undefined,
        pageNum: undefined,//当前页
        limit: undefined,//每页显示条数
        total: undefined,//总数
        pages: undefined,//总页数
        userList: ''//对象列表
    },
    mounted: function () {//加载HTML页面渲染之后自动执行
        this.getUserList();
    },
    methods: {
        getUserList: function () {//使用mounted时，组件还在挂载还在new vue所以vue对象还没拿到 所以这里必须使用this.$data来获取vue里的data
            axios.post('user/selectUserList', this.$data).then(function (response) {
                userListVue.pageNum = response.data.data.pageNum;
                userListVue.total = response.data.data.total;
                userListVue.pages = response.data.data.pages;
                userListVue.userList = response.data.data.list;
            })
        },
        previousPage: function () {
            if (this.pageNum > 1) {
                this.pageNum--;
                this.getUserList();
            } else {
                return false;
            }
        },
        nextPage: function () {
            if (this.pages - this.pageNum > 0) {
                this.pageNum++;
                this.getUserList();
            } else {
                return false;
            }
        }
    }
});