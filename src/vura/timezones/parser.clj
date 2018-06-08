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


(def zone-grammar
  "number = #'[0-9]+'
   <comment> = #'#.*'
   <word> = #'[a-zA-Z]+'
   <space> = #'[\\s\\t]+'
   <newline>= '\\n' | '\\n\\r'
   year = number
   month-name = 'Jan' | 'Feb' | 'Mar' | 'Apr' | 'May' | 'Jun' | 'Jul' | 'Aug' | 'Sep' | 'Oct' | 'Nov' | 'Dec'
   month = month-name
   day = number
   second = number
   minute = number
   hour = number 
   time-suffix = #'s|g|u|z'
   time = hour <':'> minute (<':'> second)? time-suffix?

   <zone-start> =  'Zone'
   sign = '+' | '-'
   gmtoff = sign? hour ':' minute (':' second)? 
   zone-name = #'[a-zA-Z/_\\-]+'
   zone-rule = #'[a-zA-Z\\-]+' 
   zone-format = #'[A-Z\\-/%s]+'
   zone-until = year (<space> month <space> day)? (<space> time)?
   zone-row = <space> gmtoff <space> zone-rule <space> zone-format (<space> zone-until <newline>?)?
   zone-definition = <zone-start> <space> zone-name zone-row+ 
   
   link-alias = zone-name 
   link-canonical = zone-name
   link-definition = <'Link'> <space> link-canonical <space> link-alias <newline>

   rule-name = #'[a-zA-Z\\-_]+'
   rule-type = '-' | word
   rule-from = year
   rule-to = year | 'only'
   rule-in = month
   rule-on = day
   rule-at = time
   rule-save = time
   rule-letters = word
   rule-definition = <'Rule'> <space> rule-name <space> rule-from <space> rule-to <space> rule-type <space> rule-in <space> rule-on <space> rule-at <space> rule-save <space> rule-letters
   <empty-space> = (<space> <newline> | <newline>)+
   timezone = (rule-definition /  zone-definition / link-definition)+")

(def zone-definition 
  "# Summer Time Order 1997 (S.I. 1997/2982)
# See EU for rules starting in 1996.
#
# Use Europe/London for Jersey, Guernsey, and the Isle of Man.

# Zone	NAME		GMTOFF	RULES	FORMAT	[UNTIL]
Zone	Europe/London	-0:01:15 -	LMT	1847 Dec  1  0:00s
			0:00	GB-Eire	%s	1968 Oct 27
			1:00	-	BST	1971 Oct 31  2:00u
			0:00	GB-Eire	%s	1996
			0:00	EU	GMT/BST")


(def rule-definition "Rule	GB-Eire	1916	only	-	May	21	2:00s	1:00	BST")
(def zone-parser (insta/parser zone-grammar))

(comment
  (zone-parser rule-definition :start :rule-definition)
  (->
    zone-definition
    (clojure.string/replace #"#.*" "")
    clojure.string/split-lines
    (#(remove (comp empty? clojure.string/trim) %))
    (#(clojure.string/join "\n" %))
    (zone-parser :start :zone-definition)))

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
