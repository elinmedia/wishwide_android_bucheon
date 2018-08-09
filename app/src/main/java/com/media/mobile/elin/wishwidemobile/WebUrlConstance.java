package com.media.mobile.elin.wishwidemobile;

public interface WebUrlConstance {
    //Webserver Domin Name
    String DOMAIN_NAME = "http://45.119.147.60:3000/";    //실서버
//    String DOMAIN_NAME = "http://localhost:32888/";  //테스트 서버1
//    String DOMAIN_NAME = "http://192.168.1.102:8080/";  //테스트 서버2

    //String AUTO_LOGIN_PATH = "mobile/user/loginAuto";
    String JOIN_PATH = "user";
    String HOME_PATH = "home";

    String STAMP_AND_POINT_LIST_DETAIL_PATH = "benefit/detailStampAndPoint";
    String STAMP_AND_POINT_LIST_HISTORY_PATH = "benefit/historyStampAndPoint";
    String RECEIVED_GIFT_LIST_PATH = "benefit/listReceiveGift";
    String RECEIVED_GIFT_DETIL_PATH = "benefit/detailReceiveGift";
    String SEND_GIFT_LIST_PATH = "benefit/listSendGift";
    String SEND_GIFT_DETIL_PATH = "benefit/detailSendGift";
    String COUPON_LIST_PATH = "benefit/listCoupon";
    String COUPON_DETAIL_PATH = "benefit/detailCoupon";

    String GIFT_STORE_LIST_PATH = "gift/listGift";
    String GIFT_DETAIL_PATH = "gift/detailGift";
    String CONTACT_LIST_PATH = "gift/listContact";
    String GIFT_ORDER_PATH = "gift/orderGift";

//    String VISITED_STORE_LIST_PATH = "mobile/store/listVisitedStore";
//    String STORE_DETAIL_PATH = "mobile/store/detailStore";
//    String STAMP_AND_POINT_LIST_PATH = "mobile/benefit/listStampAndPoint";
//    String GIFT_DETAIL_PATH = "mobile/gift2/detailGift";
//    String GAME_SETTING_PATH = "mobile/game/searchGameSetting";
//    String GAME_BENEFIT_REGISTER_PATH = "mobile/game/registerGameBenefit";
//    String NEARBY_BEACON_LIST_PATH = "mobile/store/listNearbyBeacon";
}
