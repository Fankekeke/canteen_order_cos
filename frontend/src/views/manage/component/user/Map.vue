<template>
  <a-drawer
    placement="right"
    width="100%"
    :closable="false"
    :visible="orderShow"
    destroyOnClose
    wrapClassName="aa"
    :getContainer="false"
  >
    <div style="width: 100%">
      <a-icon type="arrow-left" style="position: absolute;z-index: 999;color: red;font-size: 20px;margin: 15px" @click="home"/>
      <a-row style="height:100vh;font-family: SimHei">
        <a-col :span="15" style="height: 100%;">
          <div style="width: 100%;height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);color:#fff">
            <a-row :gutter="20" style="padding: 50px">
              <a-col :span="8" v-for="(item, index) in dishesList" :key="index" style="margin-bottom: 15px">
                <div style="width: 100%;margin-bottom: 15px;text-align: left;box-shadow: rgba(50, 50, 93, 0.25) 0px 50px 100px -20px, rgba(0, 0, 0, 0.3) 0px 30px 60px -30px;">
                  <a-card :bordered="false" hoverable>
                    <a-carousel autoplay style="height: 150px;" v-if="item.images !== undefined && item.images !== ''">
                      <div style="width: 100%;height: 150px" v-for="(item, index) in item.images.split(',')" :key="index">
                        <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 250px">
                      </div>
                    </a-carousel>
                    <a-card-meta :title="item.name" :description="item.content.slice(0, 25)+'...'" style="margin-top: 10px"></a-card-meta>
                    <div style="font-size: 12px;font-family: SimHei;margin-top: 8px;margin-bottom: 5px">
                      <a-row>
                        <a-col :span="18">
                          <div>
                            <span>{{ item.rawMaterial }}</span> |
                            <span  style="margin-left: 2px">{{ item.taste }}</span>
                          </div>
                          <div style="color: #f5222d; font-size: 13px;float: left">{{ item.unitPrice }}元</div>
                        </a-col>
                        <a-col :span="6" style="height: 100%;text-align: right">
                          <a-icon type="plus-square" theme="twoTone" style="font-size: 20px;margin-right: 5px;margin-top: 10px;cursor: pointer;" @click="dishesAdd(item)" v-show="nextFlag == 1"/>
                        </a-col>
                      </a-row>
                    </div>
                  </a-card>
                </div>
              </a-col>
            </a-row>
          </div>
        </a-col>
        <a-col :span="9" style="height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);color:#fff">
          <div>
            <div class="scenicInfo" style="height: 100vh; overflow-y: auto;padding-left: 5px;overflow-x: hidden;color: #4a4a48;font-size: 12px;font-family: SimHei" v-if="orderData != null && nextFlag == 1">
              <a-card :title="orderData.name" :bordered="false">
                <a-row style="padding-left: 24px;padding-right: 24px;font-size: 11px;font-family: SimHei">
                  <a-col :span="24" style="margin-top: 10px;margin-bottom: 10px">
                    <a-popover placement="bottom">
                      <template slot="content">
                        <a-avatar shape="square" :size="132" icon="user" :src="'http://127.0.0.1:9527/imagesWeb/' + orderData.images.split(',')[0]" />
                      </template>
                      <a-avatar shape="square" :size="50" icon="user" :src="'http://127.0.0.1:9527/imagesWeb/' + orderData.images.split(',')[0]" />
                    </a-popover>
                  </a-col>
                  <a-col :span="8"><b>商家编号：</b>
                    {{ orderData.code }}
                  </a-col>
                  <a-col :span="8"><b>负责人：</b>
                    {{ orderData.principal ? orderData.principal : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;font-size: 11px;font-family: SimHei">
                  <a-col :span="8"><b>联系方式：</b>
                    {{ orderData.phone }}
                  </a-col>
                  <a-col :span="16"><b>详细地址：</b>
                    {{ orderData.address }}
                  </a-col>
                </a-row>
              </a-card>
              <div style="font-size: 12px;font-family: SimHei;color: #404040;margin-top: 25px">
                <div>
                  <a-row style="padding-left: 24px;padding-right: 24px;font-size: 11px;font-family: SimHei">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">购买信息</span></a-col>
                  </a-row>
                  <div v-if="checkList.length !== 0" style="font-size: 12px;font-family: SimHei">
                    <a-table :columns="columns" :rowKey="record => record.id" :data-source="checkList" :pagination="false">
                      <template slot="operation" slot-scope="text, record">
                        <a-icon type="minus-square" theme="twoTone" @click="dishesRemove(record)" title="删 除" style="cursor: pointer;"></a-icon>
                      </template>
                    </a-table>
                    <div style="padding-left: 20px;margin-top: 25px"><span>合计</span>
                    <span style="color: red">{{ totalPrice }} 元</span>
                    </div>
                  </div>
                  <div style="margin-top: 150px;text-align: center"  v-if="checkList.length === 0">
                    <a-icon type="smile" theme="twoTone" style="font-size: 75px"/>
                    <h1 style="margin-top: 20px">请选择菜品</h1>
                  </div>
                </div>
              </div>
            </div>
            <div v-if="nextFlag == 2" style="height: 100vh; overflow-y: auto;padding-left: 5px;overflow-x: hidden;color: #4a4a48;font-size: 12px;font-family: SimHei">
              <div style="font-size: 12px;font-family: SimHei;color: #404040;">
                <div v-if="type == 1" id="areas" style="width: 100%;height: 350px;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);background:#ec9e3c;color:#fff;margin-bottom: 20px"></div>
                <div style="margin-top: 25px">
                  <a-row style="padding-left: 24px;padding-right: 24px;font-size: 11px;font-family: SimHei">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">购买信息</span></a-col>
                  </a-row>
                  <div v-if="checkList.length !== 0" style="font-size: 12px;font-family: SimHei">
                    <a-table :columns="columns1" :rowKey="record => record.id" :data-source="checkList" :pagination="false">
                    </a-table>
                    <a-alert :message="'购买菜品热量【'+totalHeat+'】 超过600，请合理规划饮食' " banner v-if="totalHeat > 600"/>
                    <a-row style="padding-left: 20px;padding-right: 20px;margin-top: 30px">
                      <a-col style="margin-bottom: 15px"><span style="font-size: 13px;font-weight: 650;color: #000c17">选择 外送/堂食</span></a-col>
                      <a-col :span="24">
                        <a-radio-group button-style="solid" v-model="type">
                          <a-radio-button value="0">
                            堂食
                          </a-radio-button>
                          <a-radio-button value="1">
                            外送
                          </a-radio-button>
                        </a-radio-group>
                      </a-col>
                    </a-row>
                    <a-row style="padding-left: 20px;padding-right: 20px;margin-top: 30px"  v-if="type == 1">
                      <a-col style="margin-bottom: 15px"><span style="font-size: 13px;font-weight: 650;color: #000c17">选择 外送地址</span></a-col>
                      <a-col :span="12" v-if="type == 1">
                        <a-select v-model="addressId" style="width: 100%" @change="handleChange">
                          <a-select-option v-for="(item, index) in addressList" :value="item.id" :key="index">{{ item.address }}</a-select-option>
                        </a-select>
                      </a-col>
                    </a-row>
                    <div style="padding-left: 20px;margin-top: 25px;text-align: right;padding-right: 30px"><span>菜品合计</span>
                      <span style="color: red">{{ totalPrice }} 元</span>
                    </div>
                    <div style="padding-left: 20px;margin-top: 5px;text-align: right;padding-right: 30px" v-if="orderAddInfo.isMember == 1"><span>会员折扣</span>
                      <span style="color: red">{{ orderAddInfo.discount }} 元</span>
                    </div>
                    <div style="padding-left: 20px;margin-top: 5px;text-align: right;padding-right: 30px" v-if="orderAddInfo.addressId != null"><span>配送费用</span>
                      {{ orderAddInfo.kilometre }} 千米  <span style="color: red">{{ orderAddInfo.distributionPrice }} 元</span>
                    </div>
                    <div style="padding-left: 20px;margin-top: 5px;text-align: right;padding-right: 30px"><span>折后价格</span>
                      <span style="color: red">{{ orderAddInfo.afterOrderPrice }} 元</span>
                    </div>
                  </div>
                  <div style="margin-top: 150px;text-align: center"  v-if="checkList.length === 0">
                    <a-icon type="smile" theme="twoTone" style="font-size: 75px"/>
                    <h1 style="margin-top: 20px">请选择菜品</h1>
                  </div>
                </div>
              </div>
              <br/>
              <br/>
            </div>
          </div>
        </a-col>
      </a-row>
      <a-drawer
        title="商家评价"
        width="600"
        :closable="false"
        :visible="childrenDrawer"
        @close="onChildrenDrawerClose"
      >
        <a-list item-layout="horizontal" :data-source="evaluateList">
          <a-list-item slot="renderItem" slot-scope="item, index">
            <a-list-item-meta
              :description="item.content"
            >
              <a slot="title">{{ item.userName }}</a>
              <a-avatar
                slot="avatar"
                :src="'http://127.0.0.1:9527/imagesWeb/' + item.userImages"
              />
            </a-list-item-meta>
          </a-list-item>
        </a-list>
      </a-drawer>
      <div class="drawer-bootom-button">
        <a-button @click="showChildrenDrawer" style="margin-right: .8rem">评价信息</a-button>
        <a-popconfirm title="确定放弃编辑？" @confirm="home" okText="确定" cancelText="取消">
          <a-button style="margin-right: .8rem">取消</a-button>
        </a-popconfirm>
        <a-button @click="next" type="primary" v-if="nextFlag == 1">下一步</a-button>
        <a-button @click="orderPay" type="primary" v-if="nextFlag == 2">支付</a-button>
      </div>
    </div>
  </a-drawer>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
import {mapState} from 'vuex'
export default {
  name: 'Map',
  props: {
    orderShow: {
      type: Boolean,
      default: false
    },
    orderData: {
      type: Object
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '菜品名称',
        dataIndex: 'name'
      }, {
        title: '图片',
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
        title: '购买数量',
        dataIndex: 'amount'
      }, {
        title: '热量',
        dataIndex: 'heat'
      }, {
        title: '单价',
        dataIndex: 'unitPrice'
      }, {
        title: '总价格',
        dataIndex: 'totalPrice',
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
    },
    columns1 () {
      return [{
        title: '菜品名称',
        dataIndex: 'name'
      }, {
        title: '图片',
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
        title: '购买数量',
        dataIndex: 'amount'
      }, {
        title: '单价',
        dataIndex: 'unitPrice'
      }, {
        title: '总价格',
        dataIndex: 'totalPrice',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }]
    }
  },
  data () {
    return {
      childrenDrawer: false,
      orderAddInfo: null,
      addressId: null,
      addressList: [],
      type: '0',
      nextFlag: 1,
      totalPrice: 0,
      totalHeat: 0,
      dishesList: [],
      evaluateList: [],
      checkList: [],
      evaluateInfo: null,
      vehicleInfo: null,
      userInfo: null,
      communityRent: 0,
      rentShow: false,
      communityShow: false,
      communityData: null,
      checkedList: ['Apple', 'Orange'],
      indeterminate: true,
      checkAll: false,
      plainOptions: ['Apple', 'Pear', 'Orange'],
      visible: false,
      rentList: [],
      communityList: [],
      community: null,
      nowPoint: null,
      roadData: [],
      checkLoading: false,
      echartsShow: false,
      getShop: null,
      putShop: null,
      series: [{
        name: '得分',
        data: []
      }],
      chartOptions: {
        chart: {
          height: 380,
          type: 'radar'
        },
        title: {
          text: ''
        },
        xaxis: {
          categories: ['交付得分', '价格得分', '质量得分', '准时得分', '服务得分', '综合得分']
        }
      }
    }
  },
  watch: {
    'orderShow': function (value) {
      if (value) {
        this.checkList = []
        this.addressId = null
        this.addressList = []
        this.type = '0'
        this.nextFlag = 1
        this.totalPrice = 0
        this.totalHeat = 0
        this.selectDishesByMerchant(this.orderData.id)
        this.selectMerchantEvaluate(this.orderData.id)
        this.selectAddress()
      }
    },
    'type': function (value) {
      if (value.toString() === '1') {
        setTimeout(() => {
          baiduMap.initMap('areas')
          this.getLocal()
        }, 200)
      }
    }
  },
  methods: {
    showChildrenDrawer () {
      this.childrenDrawer = true
    },
    onChildrenDrawerClose () {
      this.childrenDrawer = false
    },
    orderPay (record) {
      this.orderAddInfo.userId = this.currentUser.userId
      this.$post('/cos/pay/alipay', this.orderAddInfo).then((r) => {
        // console.log(r.data.msg)
        // 添加之前先删除一下，如果单页面，页面不刷新，添加进去的内容会一直保留在页面中，二次调用form表单会出错
        const divForm = document.getElementsByTagName('div')
        if (divForm.length) {
          document.body.removeChild(divForm[0])
        }
        const div = document.createElement('div')
        div.innerHTML = r.data.msg // data就是接口返回的form 表单字符串
        // console.log(div.innerHTML)
        document.body.appendChild(div)
        document.forms[0].setAttribute('target', '_self') // 新开窗口跳转
        document.forms[0].submit()
      })
    },
    getPriceTotal () {
      this.$post(`/cos/order-info/getPriceTotal`, {
        userId: this.currentUser.userId,
        merchantId: this.orderData.id,
        orderPrice: this.totalPrice,
        type: this.type,
        addressId: this.addressId,
        orderItemListStr: JSON.stringify(this.checkList)
      }).then((r) => {
        this.orderAddInfo = r.data.data
      })
    },
    handleChange (value) {
      if (value) {
        this.getPriceTotal()
        let addressList = this.addressList.filter(e => e.id === value)
        this.navigation(addressList[0])
      }
    },
    selectAddress () {
      this.$get(`/cos/address-info/listByUserId/${this.currentUser.userId}`).then((r) => {
        this.addressList = r.data.data
      })
    },
    next () {
      if (this.checkList.length === 0) {
        this.$message.warn('请选择菜品信息')
        return false
      }
      this.nextFlag = 2
      this.getPriceTotal()
    },
    dishesRemove (row) {
      let checkList = this.checkList
      this.checkList = []
      checkList.forEach(e => {
        if (e.id === row.id) {
          e.amount = e.amount - 1
          e.totalPrice = (e.unitPrice * e.amount).toFixed(2)
          e.totalHeat = (e.heat * e.amount).toFixed(2)
          if (e.amount === 0) {
            checkList = checkList.filter(e => e.id !== row.id)
          }
        }
      })
      let totalPrice = 0
      let totalHeat = 0
      checkList.forEach(e => {
        totalPrice = Number(e.totalPrice) + Number(totalPrice)
        totalHeat = Number(e.totalHeat) + Number(totalHeat)
      })
      this.totalPrice = totalPrice.toFixed(2)
      this.totalHeat = totalHeat.toFixed(2)
      this.checkList = checkList
    },
    dishesAdd (row) {
      row.dishesId = row.id
      let checkList = this.checkList
      this.checkList = []
      let check = false
      checkList.forEach(e => {
        e.dishesId = e.id
        if (e.id === row.id) {
          check = true
          e.amount = e.amount + 1
          e.totalPrice = (e.unitPrice * e.amount).toFixed(2)
          e.totalHeat = (e.heat * e.amount).toFixed(2)
        }
      })
      if (!check) {
        let data = row
        data.amount = 1
        data.totalPrice = data.unitPrice
        data.totalHeat = data.heat
        checkList.push(data)
      }
      let totalPrice = 0
      let totalHeat = 0
      checkList.forEach(e => {
        totalPrice = Number(e.totalPrice) + Number(totalPrice)
        totalHeat = Number(e.totalHeat) + Number(totalHeat)
      })
      this.totalPrice = totalPrice.toFixed(2)
      this.totalHeat = totalHeat.toFixed(2)
      this.checkList = checkList
    },
    selectDishesByMerchant (merchantId) {
      this.$get(`/cos/dishes-info/selectDishesByMerchant/${merchantId}`).then((r) => {
        this.dishesList = r.data.data
      })
    },
    selectMerchantEvaluate (merchantId) {
      this.$get(`/cos/merchant-info/selectEvaluateByMerchant`, {merchantId}).then((r) => {
        this.evaluateList = r.data.data
      })
    },
    navigation (data) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions: {map: baiduMap.rMap(), autoViewport: true}})
      // eslint-disable-next-line no-undef
      driving.search(new BMap.Point(this.orderData.longitude, this.orderData.latitude), new BMap.Point(data.longitude, data.latitude))
      // this.getRoadData()
    },
    getRoadData () {
      let options = {
        onSearchComplete: results => {
          // eslint-disable-next-line eqeqeq,no-undef
          if (driving.getStatus() == BMAP_STATUS_SUCCESS) {
            // 获取第一条方案
            let plan = results.getPlan(0)
            // 获取方案的驾车线路
            // eslint-disable-next-line no-unused-vars
            let route = plan.getRoute(0)
            // 获取每个关键步骤,并输出到页面
            let s = []
            for (let j = 0; j < plan.getNumRoutes(); j++) {
              let route = plan.getRoute(j)
              for (let i = 0; i < route.getNumSteps(); i++) {
                let step = route.getStep(i)
                s.push((i + 1) + '. ' + step.getDescription())
              }
            }
            this.roadData = s
          }
        }
      }
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), options)
      // eslint-disable-next-line no-undef
      let start = new BMap.Point(this.nowPoint.lng, this.nowPoint.lat)
      let end = null
      if (this.rentShow) {
        end = new BMap.Point(this.rentData.longitude, this.rentData.latitude)
      } else {
        end = new BMap.Point(this.communityData.longitude, this.communityData.latitude)
      }
      // eslint-disable-next-line no-undef
      driving.search(start, end)
    },
    checkEvaluate (score) {
      let data = [score.deliverScore, score.priceScore, score.qualityScore, score.scheduleScore, score.serviceScore, score.overScore, 100]
      this.series[0].data = data
    },
    home () {
      this.$emit('close')
    },
    gisOnChange (e) {
      let data = null
      switch (e.target.value) {
        case '1':
          data = this.getShop
          break
        case '2':
          data = this.putShop
          break
      }
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions: {map: baiduMap.rMap(), autoViewport: true}})
      // eslint-disable-next-line no-undef
      driving.search(new BMap.Point(this.nowPoint.lng, this.nowPoint.lat), new BMap.Point(data.longitude, data.latitude))
    },
    getLocal () {
      // eslint-disable-next-line no-undef
      let geolocation = new BMap.Geolocation()
      geolocation.getCurrentPosition(r => {
        this.nowPoint = r.point
      }, {enableHighAccuracy: true})
    }
  }
}
</script>

<style scoped>
  >>> .ant-drawer-body {
    padding: 0 !important;
  }
  >>> .ant-card-meta-title {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-card-meta-description {
    font-size: 12px;
    font-family: SimHei;
  }
  >>> .ant-divider-with-text-left {
    margin: 0;
  }

  >>> .ant-card-head-title {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-card-extra {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-radio-button-wrapper {
    border-radius: 0;
  }

  >>> .ant-card-meta-title {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-card-meta-description {
    font-size: 12px;
    font-family: SimHei;
  }
  >>> .ant-divider-with-text-left {
    margin: 0;
  }

  >>> .ant-card-head-title {
    font-size: 13px;
    font-family: SimHei;
  }
  >>> .ant-card-extra {
    font-size: 13px;
    font-family: SimHei;
  }
  .ant-carousel >>> .slick-slide {
    text-align: center;
    height: 150px;
    line-height: 150px;
    overflow: hidden;
  }
</style>
