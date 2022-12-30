;; This file is autogenerated using vura.holidays.compile/-main

(ns vura.holidays.vn
  (:require 
  [clojure.string]
  [vura.holidays :refer [is-holiday?]]
  [vura.holidays.name :as n]
  [vura.holidays.compiler :as compiler]))


(def holidays
  {"vietnamese 7-0-15"
 {"type" "observance",
  :name {:en "Ghost Festival", :vi "Rằm Tháng Bảy, Vu Lan"}},
 "06-01"
 {"type" "observance",
  :name
  {:en "International Children's Day", :vi "Ngày quốc tế Thiếu nhi"}},
 "vietnamese 12-0-23"
 {"type" "observance",
  :name {:en "Kitchen guardians", :vi "Ông Táo chầu trời"}},
 "11-20"
 {"type" "observance",
  :name
  {:en "Vietnamese Teacher's Day", :vi "Ngày Nhà giáo Việt Nam"}},
 "04-30"
 {:name
  {:en "Day of liberating the South for national reunification",
   :vi "Ngày Giải phóng miền Nam"}},
 "01-01" {:name (partial n/get-name "01-01")},
 "vietnamese 1-0-1"
 {:name {:en "Vietnamese New Year", :vi "Tết Nguyên Đán"}},
 "07-27"
 {"type" "observance",
  :name {:en "Remembrance Day", :vi "Ngày Thương Binh Liệt Sĩ"}},
 "vietnamese 4-0-15"
 {"type" "observance",
  :name {:en "Buddha's Birthday", :vi "Lễ Phật đản"}},
 "02-03"
 {"type" "observance",
  :name
  {:en "Communist Party of Viet Nam Foundation Anniversary",
   :vi "Ngày thành lập Đảng"}},
 "10-10"
 {"type" "observance",
  :name {:en "Capital Liberation Day", :vi "Ngày giải phóng Thủ Đô"}},
 "vietnamese 3-0-10"
 {:name {:en "Hung Kings Commemorations", :vi "Giỗ tổ Hùng Vương"}},
 "09-02" {:name {:en "National Day", :vi "Quốc khánh"}},
 "vietnamese 8-0-15"
 {"type" "observance",
  :name {:en "Mid-Autumn Festival", :vi "Tết Trung thu"}},
 "10-20"
 {"type" "observance",
  :name {:en "Vietnamese Women's Day", :vi "Ngày Phụ nữ Việt Nam"}},
 "1 day before vietnamese 1-0-1 P5D"
 {:name
  {:en "Vietnamese New Year Holidays",
   :vi "Giao thừa Tết Nguyên Đán"}},
 "08-19"
 {"type" "observance",
  :name
  {:en "August Revolution Commemoration Day",
   :vi "Ngày cách mạng Tháng Tám"}},
 "03-08" {"type" "observance", :name (partial n/get-name "03-08")},
 "vietnamese 1-0-5"
 {"type" "observance",
  :name
  {:en "Victory of Ngọc Hồi-Đống Đa",
   :vi "Chiến thắng Ngọc Hồi - Đống Đa"}},
 "05-01" {:name (partial n/get-name "05-01")},
 "vietnamese 5-0-5"
 {"type" "observance",
  :name {:en "Mid-year Festival", :vi "Tết Đoan ngọ"}},
 "vietnamese 1-0-15"
 {"type" "observance",
  :name {:en "Lantern Festival", :vi "Tết Nguyên Tiêu"}},
 "05-07"
 {"type" "observance",
  :name
  {:en "Dien Bien Phu Victory Day",
   :vi "Ngày Chiến thắng Điện Biện Phủ"}},
 "04-21"
 {"type" "observance",
  :name {:en "Vietnam Book Day", :vi "Ngày Sách Việt Nam"}},
 "06-28"
 {"type" "observance",
  :name {:en "Vietnamese Family Day", :vi "Ngày Gia đình Việt Nam"}},
 "05-19"
 {"type" "observance",
  :name
  {:en "President Ho Chi Minh's Birthday",
   :vi "Ngày sinh Chủ tịch Hồ Chí Minh"}},
 "12-22"
 {"type" "observance",
  :name
  {:en "National Defence Day", :vi "Ngày hội Quốc phòng Toàn dân"}}}
)


(def locale-holiday-mapping
  (reduce-kv
    (fn [result definition name-mapping]
      (assoc result
             (compiler/compile-type (compiler/parse-definition definition))
             name-mapping))
    nil
    holidays))


(defn holiday?
  [context]
  (some
   (fn [[pred naming]]
     (when (pred context)
       naming))
   locale-holiday-mapping))


(defmethod is-holiday? :vn
  [_ context]
  (holiday? context))