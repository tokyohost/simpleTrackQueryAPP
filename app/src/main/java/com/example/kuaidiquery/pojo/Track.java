package com.example.kuaidiquery.pojo;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

public class Track implements Serializable {

    /**
     * LogisticCode : 773036187850410
     * ShipperCode : STO
     * Traces : [{"AcceptStation":"广东深圳福田石岩二部-李仕鹏(18126238070)(18126238070)-已收件","AcceptTime":"2020-05-13 13:30:02"},{"AcceptStation":"已到达-广东深圳福田转运中心","AcceptTime":"2020-05-13 21:46:24"},{"AcceptStation":"已到达-广东深圳福田转运中心","AcceptTime":"2020-05-13 21:56:52"},{"AcceptStation":"广东深圳福田转运中心-已发往-江苏淮安转运中心","AcceptTime":"2020-05-13 21:57:47"},{"AcceptStation":"广东深圳福田转运中心-已发往-江苏淮安转运中心","AcceptTime":"2020-05-13 22:06:29"},{"AcceptStation":"已到达-广东深圳福田转运中心","AcceptTime":"2020-05-13 22:18:28"},{"AcceptStation":"广东深圳福田转运中心-已发往-广东深圳转运中心","AcceptTime":"2020-05-13 22:24:37"},{"AcceptStation":"已到达-广东深圳转运中心","AcceptTime":"2020-05-13 23:59:53"},{"AcceptStation":"广东深圳转运中心-已发往-江苏淮安转运中心","AcceptTime":"2020-05-14 00:12:23"},{"AcceptStation":"已到达-江苏淮安转运中心","AcceptTime":"2020-05-15 05:21:22"},{"AcceptStation":"江苏淮安转运中心-已发往-江苏连云港公司","AcceptTime":"2020-05-15 05:35:03"},{"AcceptStation":"已到达-江苏连云港公司","AcceptTime":"2020-05-15 11:44:15"},{"AcceptStation":"江苏连云港公司-花果山大学城(13003470518)(13003470518)-派件中","AcceptTime":"2020-05-15 11:53:18"},{"AcceptStation":"快件已被【连云港市职业技术学院\t速递易J23自提柜】代收，请及时取件。如有疑问，请联系派件员13003470518","AcceptTime":"2020-05-15 15:22:20"},{"AcceptStation":"客户签收：已签收，签收人凭取货码签收","AcceptTime":"2020-05-15 17:08:13"}]
     * State : 3
     * EBusinessID : 1644476
     * Success : true
     */

    private String LogisticCode;
    private String ShipperCode;
    private String State;
    private String EBusinessID;
    private boolean Success;
    private List<TracesBean> Traces;

    public static Track objectFromData(String str) {

        return new Gson().fromJson(str, Track.class);
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String LogisticCode) {
        this.LogisticCode = LogisticCode;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String ShipperCode) {
        this.ShipperCode = ShipperCode;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public List<TracesBean> getTraces() {
        return Traces;
    }

    public void setTraces(List<TracesBean> Traces) {
        this.Traces = Traces;
    }

    public static class TracesBean {
        /**
         * AcceptStation : 广东深圳福田石岩二部-李仕鹏(18126238070)(18126238070)-已收件
         * AcceptTime : 2020-05-13 13:30:02
         */

        private String AcceptStation;
        private String AcceptTime;

        public static TracesBean objectFromData(String str) {

            return new Gson().fromJson(str, TracesBean.class);
        }

        public String getAcceptStation() {
            return AcceptStation;
        }

        public void setAcceptStation(String AcceptStation) {
            this.AcceptStation = AcceptStation;
        }

        public String getAcceptTime() {
            return AcceptTime;
        }

        public void setAcceptTime(String AcceptTime) {
            this.AcceptTime = AcceptTime;
        }
    }
}
