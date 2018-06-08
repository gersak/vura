(ns vura.timezones.parser
  (:refer-clojure :exclude [second])
  (:require [vura.core :refer :all]
            [instaparse.core :as insta]))

(def europe (slurp "resources/timezones/tzdb-2018e/europe"))

(defn break-timezone-db 
  "Breaks timezone text to zones, rules and links"
  [text]
  (letfn [(read-zone-definition [rlines]
            (loop [lines rlines
                   definition []]
              (let [[line & rlines] lines] 
                (if (or
                      (empty? line)
                      (clojure.string/starts-with? line "Link")
                      (clojure.string/starts-with? line "Zone"))
                  [definition rlines]
                  (recur rlines (conj definition line))))))] 
    (let [text (clojure.string/replace text #"#.*" "")
          lines (clojure.string/split-lines text)]
      (loop [lines lines
             zones []
             links []
             rules []]
        (if (empty? lines) {:zones zones
                            :links links
                            :rules rules}
          (let [[line & rlines] lines]
            (cond
              ;;
              (clojure.string/starts-with? line "Zone")
              (let [[definition rlines] (read-zone-definition rlines)]
                (recur rlines (conj zones (concat [line] definition)) links rules))
              ;;
              (clojure.string/starts-with? line "Link")
              (recur rlines zones (conj links line) rules)
              ;;
              (clojure.string/starts-with? line "Rule")
              (recur rlines zones links (conj rules line))
              ;;
              :else
              (recur rlines zones links rules))))))))

(def months
  {"Jan" 1
   "Feb" 2
   "Mar" 3
   "Apr" 4
   "May" 5
   "Jun" 6
   "Jul" 7
   "Aug" 8
   "Sep" 9
   "Oct" 10
   "Nov" 11
   "Dec" 12})

(def from-to-grammar
  "number = #'[0-9]+'
   <word> = #'[a-zA-Z]+'
   <space> = #'\\s+'
   year = number
   month-name = 'Jan' | 'Feb' | 'Mar' | 'Apr' | 'May' | 'Jun' | 'Jul' | 'Aug' | 'Sep' | 'Oct' | 'Nov' | 'Dec'
   month = month-name
   day = number
   second = number
   minute = number
   hour = number 
   time-suffix = #'s|g|u|z'
   time = hour <':'> minute (<':'> second)? time-suffix")



(defn until->date [text]
  (let [grammar (apply str
                       "until = year (<space> month <space> day)? (<space> time)?\n"
                       from-to-grammar)
        parser (insta/parser grammar)
        result (parser text)]
    (insta/transform
      {:number #(Integer/parseInt %)
       :month-name months
       :time (fn [& args] (reduce conj {} args))
       :until (fn [& args] (reduce conj {} args))}
      result)))

(defn parse-zone-definition [lines]
  (let [[zd & zdl] lines
        [_ zname gmtoff zformat zuntil] (clojure.string/split zd #"\t")
        [gmtoff rule] (clojure.string/split gmtoff #"\s+")
        definition {:name zname
                    :history [{:offset gmtoff :rule rule :format zformat :until zuntil}]}]
    (if (empty? zdl) definition
      (reduce 
        (fn [r [gmt rule zformat until]]
          (update r :history conj 
            {:offset gmt :rule rule :format zformat :until until}))
        definition
        (map
          #(clojure.string/split (clojure.string/trim %) #"\t")
          zdl)))))


(defn parse-link-definition [line]
  (let [[_ from to] (clojure.string/split line #"[\t\s]+")]
    [to from]))

(defn parse-rule-definition [line]
  (let [[_ & data] (clojure.string/split line #"[\t\s]+")]
    (zipmap 
      [:name :from :to :type :in :on :at :save :letter]
      data)))



(comment
  (def ise (break-timezone-db europe))
  (:links ise)
  (:rules ise)
  (def until (-> ise :zones first parse-zone-definition :history first))
  (re-find #"(\d+)\s+(\w+)\s+(\d+)\s+(\d+):(\d+[sguz])" "1847 Dec  1  0:00s")
  (-> ise :links first parse-link-definition)
  (-> ise :rules first parse-rule-definition))
