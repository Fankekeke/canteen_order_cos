<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="菜品编号"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.code"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="菜品名称"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.name"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="商家名称"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.merchantName"/>
              </a-form-item>
            </a-col>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <a-button type="primary" ghost @click="add">新增</a-button>
        <a-button @click="batchDelete">删除</a-button>
      </div>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :columns="columns"
               :rowKey="record => record.id"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
               :scroll="{ x: 900 }"
               @change="handleTableChange">
        <template slot="operation" slot-scope="text, record">
          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改"></a-icon>
          <a-icon type="file-search" @click="dishesViewOpen(record)" title="详 情" style="margin-left: 15px"></a-icon>
          <a-icon type="reconciliation" @click="showModal(record)" title="添加库存" style="margin-left: 15px"></a-icon>
        </template>
      </a-table>
    </div>
    <dishes-add
      v-if="dishesAdd.visiable"
      @close="handledishesAddClose"
      @success="handledishesAddSuccess"
      :dishesAddVisiable="dishesAdd.visiable">
    </dishes-add>
    <dishes-edit
      ref="dishesEdit"
      @close="handledishesEditClose"
      @success="handledishesEditSuccess"
      :dishesEditVisiable="dishesEdit.visiable">
    </dishes-edit>
    <dishes-view
      @close="handledishesViewClose"
      :dishesShow="dishesView.visiable"
      :dishesData="dishesView.data">
    </dishes-view>
    <a-modal
      title="添加库存"
      :visible="visible"
      :confirm-loading="confirmLoading"
      :width="950"
      @ok="auditQuota"
      @cancel="handleCancel"
    >
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="dishesInfo !== null">
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
          <a-col :span="8"><b>菜品编号：</b>
            {{ dishesInfo.code }}
          </a-col>
          <a-col :span="8"><b>菜品名称：</b>
            {{ dishesInfo.name ? dishesInfo.name : '- -' }}
          </a-col>
          <a-col :span="8"><b>原料：</b>
            {{ dishesInfo.rawMaterial ? dishesInfo.rawMaterial : '- -' }}
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col :span="8"><b>份量：</b>
            {{ dishesInfo.portion }}
          </a-col>
          <a-col :span="8"><b>口味：</b>
            {{ dishesInfo.taste }}
          </a-col>
          <a-col :span="8"><b>价格：</b>
            {{ dishesInfo.unitPrice }} 元
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col :span="8"><b>余量：</b>
            {{ dishesInfo.laveNum }}
          </a-col>
          <a-col :span="8"><b>上架状态：</b>
            <span v-if="dishesInfo.status == 0" style="color: red">下架</span>
            <span v-if="dishesInfo.status == 1" style="color: green">上架</span>
          </a-col>
          <a-col :span="8"><b>创建时间：</b>
            {{ dishesInfo.createDate }}
          </a-col>
        </a-row>
        <a-col style="margin-bottom: 15px;margin-top: 50px"><span style="font-size: 15px;font-weight: 650;color: #000c17">添加库存:</span></a-col>
        <a-input-number style="width: 100%" :min="1" :step="1" v-model="quota"/>
      </a-row>
    </a-modal>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import dishesAdd from './DishesAdd'
import dishesEdit from './DishesEdit'
import dishesView from './DishesView.vue'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'dishes',
  components: {dishesAdd, dishesEdit, dishesView, RangeDate},
  data () {
    return {
      visible: false,
      confirmLoading: false,
      advanced: false,
      dishesAdd: {
        visiable: false
      },
      dishesEdit: {
        visiable: false
      },
      dishesView: {
        visiable: false,
        data: null
      },
      quota: 1,
      queryParams: {},
      filteredInfo: null,
      dishesInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      userList: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '菜品编号',
        dataIndex: 'code'
      }, {
        title: '菜品名称',
        dataIndex: 'name'
      }, {
        title: '菜品图片',
        dataIndex: 'images',
        customRender: (text, record, index) => {
          if (!record.images) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
          </a-popover>
        }
      }, {
        title: '商家名称',
        dataIndex: 'merchantName',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '商家图片',
        dataIndex: 'merchantImages',
        customRender: (text, record, index) => {
          if (!record.merchantImages) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.merchantImages.split(',')[0] } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.merchantImages.split(',')[0] } />
          </a-popover>
        }
      }, {
        title: '原料',
        dataIndex: 'rawMaterial',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '份量',
        dataIndex: 'portion',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '口味',
        dataIndex: 'taste',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '价格',
        dataIndex: 'unitPrice',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text + '元'
          } else {
            return '- -'
          }
        }
      }, {
        title: '状态',
        dataIndex: 'status',
        customRender: (text, row, index) => {
          switch (text) {
            case '0':
              return <a-tag color="red">下架</a-tag>
            case '1':
              return <a-tag color="green">上架</a-tag>
            default:
              return '- -'
          }
        }
      }, {
        title: '余量',
        dataIndex: 'laveNum',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    showModal (row) {
      this.dishesInfo = row
      this.visible = true
    },
    handleCancel (e) {
      console.log('Clicked cancel button')
      this.visible = false
    },
    auditQuota () {
      this.$post('/cos/dishes-info/auditQuota', { dishesId: this.dishesInfo.id, num: this.quota }).then((r) => {
        this.$message.success('修改成功')
        this.visible = false
        this.fetch()
      })
    },
    dishesViewOpen (row) {
      this.dishesView.data = row
      this.dishesView.visiable = true
    },
    handledishesViewClose () {
      this.dishesView.visiable = false
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add () {
      this.dishesAdd.visiable = true
    },
    handledishesAddClose () {
      this.dishesAdd.visiable = false
    },
    handledishesAddSuccess () {
      this.dishesAdd.visiable = false
      this.$message.success('新增菜品成功')
      this.search()
    },
    edit (record) {
      this.$refs.dishesEdit.setFormValues(record)
      this.dishesEdit.visiable = true
    },
    handledishesEditClose () {
      this.dishesEdit.visiable = false
    },
    handledishesEditSuccess () {
      this.dishesEdit.visiable = false
      this.$message.success('修改菜品成功')
      this.search()
    },
    handleDeptChange (value) {
      this.queryParams.deptId = value || ''
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/cos/dishes-info/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    search () {
      let {sortedInfo, filteredInfo} = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
        ...filteredInfo
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列过滤器规则
      this.filteredInfo = null
      // 重置列排序规则
      this.sortedInfo = null
      // 重置查询参数
      this.queryParams = {}
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      // 将这三个参数赋值给Vue data，用于后续使用
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter

      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams,
        ...filters
      })
    },
    fetch (params = {}) {
      // 显示loading
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.size = this.paginationInfo.pageSize
        params.current = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.size = this.pagination.defaultPageSize
        params.current = this.pagination.defaultCurrent
      }
      params.merchantId = this.currentUser.userId
      this.$get('/cos/dishes-info/page', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataSource = data.records
        this.pagination = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  },
  watch: {}
}
</script>
<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
