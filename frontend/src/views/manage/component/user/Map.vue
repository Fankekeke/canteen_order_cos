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
                          <a-icon type="plus-square" theme="twoTone" style="font-size: 20px;margin-right: 5px;margin-top: 10px;cursor: pointer;" @click="dishesAdd(item)"/>
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
                    <a-table :columns="columns" :rowKey="record => record.id" :data-source="checkList" :pagination="false">
                      <template slot="operation" slot-scope="text, record">
                        <a-icon type="minus-square" theme="twoTone" @click="dishesRemove(record)" title="删 除" style="cursor: pointer;"></a-icon>
                      </template>
                    </a-table>
                    <div style="padding-left: 20px;margin-top: 25px;text-align: right;padding-right: 30px"><span>合计</span>
                      <span style="color: red">{{ totalPrice }} 元</span>
                    </div>
                  </div>
                  <div style="margin-top: 150px;text-align: center"  v-if="checkList.length === 0">
                    <a-icon type="smile" theme="twoTone" style="font-size: 75px"/>
                    <h1 style="margin-top: 20px">请选择菜品</h1>
                  </div>
                </div>
              </div>
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
                  <a-select v-model="addressId" style="width: 100%">
                    <a-select-option v-for="(item, index) in addressList" :value="item.id" :key="index">{{ item.address }}</a-select-option>
                  </a-select>
                </a-col>
              </a-row>
              <br/>
              <br/>
            </div>
          </div>
        </a-col>
      </a-row>
      <div class="drawer-bootom-button">
        <a-popconfirm title="确定放弃编辑？" @confirm="home" okText="确定" cancelText="取消">
          <a-button style="margin-right: .8rem">取消</a-button>
        </a-popconfirm>
        <a-button @click="next" type="primary" v-if="nextFlag == 1">下一步</a-button>
        <a-button @click="pay" type="primary" v-if="nextFlag == 2">支付</a-button>
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
    }
  },
  data () {
    return {
      addressId: null,
      addressList: [],
      type: '0',
      nextFlag: 1,
      totalPrice: 0,
      dishesList: [],
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
        this.selectDishesByMerchant(this.orderData.id)
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
    selectAddress () {
      this.$get(`/cos/address-info/listByUserId/${this.currentUser.userId}`).then((r) => {
        this.addressList = r.data.data
      })
    },
    next () {
      this.nextFlag = 2
    },
    dishesRemove (row) {
      let checkList = this.checkList
      this.checkList = []
      checkList.forEach(e => {
        if (e.id === row.id) {
          e.amount = e.amount - 1
          e.totalPrice = (e.unitPrice * e.amount).toFixed(2)
          if (e.amount === 0) {
            checkList = checkList.filter(e => e.id !== row.id)
          }
        }
      })
      let totalPrice = 0
      checkList.forEach(e => {
        totalPrice = Number(e.totalPrice) + Number(totalPrice)
      })
      this.totalPrice = totalPrice.toFixed(2)
      this.checkList = checkList
    },
    dishesAdd (row) {
      let checkList = this.checkList
      this.checkList = []
      let check = false
      checkList.forEach(e => {
        if (e.id === row.id) {
          check = true
          e.amount = e.amount + 1
          e.totalPrice = (e.unitPrice * e.amount).toFixed(2)
        }
      })
      if (!check) {
        let data = row
        data.amount = 1
        data.totalPrice = data.unitPrice
        checkList.push(data)
      }
      let totalPrice = 0
      checkList.forEach(e => {
        totalPrice = Number(e.totalPrice) + Number(totalPrice)
      })
      this.totalPrice = totalPrice.toFixed(2)
      this.checkList = checkList
    },
    selectDishesByMerchant (merchantId) {
      this.$get(`/cos/dishes-info/selectDishesByMerchant/${merchantId}`).then((r) => {
        this.dishesList = r.data.data
      })
    },
    dataInit (orderId) {
      this.checkLoading = true
      this.$get(`/cos/vehicle-info/order/detail/${orderId}`).then((r) => {
        this.userInfo = r.data.user
        this.orderInfo = r.data.order
        this.vehicleInfo = r.data.vehicle
        this.getShop = r.data.getShop
        this.putShop = r.data.putShop
        this.checkLoading = false
      })
    },
    navigation (data) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions: {map: baiduMap.rMap(), autoViewport: true}})
      // eslint-disable-next-line no-undef
      driving.search(new BMap.Point(data.startLongitude, data.startLatitude), new BMap.Point(data.endLongitude, data.endLatitude))
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
