(ns vura.timezones.db)

(declare db locales timezone->locale)


(defn get-rule [rule]
  (get-in db [:rules rule]))


(defn get-zone [zone]
  (if-let [zone' (get-in db [:zones zone])]
    (if (string? zone') (get-zone zone')
      zone')
    (throw
      (ex-info "No such zone."
      {:zone zone
       :available-zones (keys (:zones db))}))))


(def available-zones
  (sort (keys (:zones db))))


(defn get-locale-timezone [locale]
  (get-in 
    locales 
    [(clojure.string/upper-case locale) :zone]))

(defn get-timezone-locale [timezone]
  (get timezone->locale timezone))


(defn get-locale [locale]
  (get locales (clojure.string/upper-case locale)))


(def locales
  {"CR" {:coordinates "+0956-08405", :zone "America/Costa_Rica"},
   "TG" {:coordinates "+0608+00113", :zone "Africa/Lome"},
   "TJ" {:coordinates "+3835+06848", :zone "Asia/Dushanbe"},
   "ZA" {:coordinates "-2615+02800", :zone "Africa/Johannesburg"},
   "IM" {:coordinates "+5409-00428", :zone "Europe/Isle_of_Man"},
   "PE" {:coordinates "-1203-07703", :zone "America/Lima"},
   "LC" {:coordinates "+1401-06100", :zone "America/St_Lucia"},
   "CH" {:coordinates "+4723+00832", :zone "Europe/Zurich"},
   "RU" {:coordinates "+6445+17729", :zone "Asia/Anadyr"},
   "MP" {:coordinates "+1512+14545", :zone "Pacific/Saipan"},
   "CK" {:coordinates "-2114-15946", :zone "Pacific/Rarotonga"},
   "SI" {:coordinates "+4603+01431", :zone "Europe/Ljubljana"},
   "AU" {:coordinates "-3143+12852", :zone "Australia/Eucla"},
   "KR" {:coordinates "+3733+12658", :zone "Asia/Seoul"},
   "IT" {:coordinates "+4154+01229", :zone "Europe/Rome"},
   "FI" {:coordinates "+6010+02458", :zone "Europe/Helsinki"},
   "GF" {:coordinates "+0456-05220", :zone "America/Cayenne"},
   "SC" {:coordinates "-0440+05528", :zone "Indian/Mahe"},
   "SX" {:coordinates "+180305-0630250", :zone "America/Lower_Princes"},
   "TT" {:coordinates "+1039-06131", :zone "America/Port_of_Spain"},
   "TK" {:coordinates "-0922-17114", :zone "Pacific/Fakaofo"},
   "MY" {:coordinates "+0133+11020", :zone "Asia/Kuching"},
   "SY" {:coordinates "+3330+03618", :zone "Asia/Damascus"},
   "MN" {:coordinates "+4804+11430", :zone "Asia/Choibalsan"},
   "TF" {:coordinates "-492110+0701303", :zone "Indian/Kerguelen"},
   "KP" {:coordinates "+3901+12545", :zone "Asia/Pyongyang"},
   "AM" {:coordinates "+4011+04430", :zone "Asia/Yerevan"},
   "DZ" {:coordinates "+3647+00303", :zone "Africa/Algiers"},
   "UY" {:coordinates "-345433-0561245", :zone "America/Montevideo"},
   "TD" {:coordinates "+1207+01503", :zone "Africa/Ndjamena"},
   "DJ" {:coordinates "+1136+04309", :zone "Africa/Djibouti"},
   "BI" {:coordinates "-0323+02922", :zone "Africa/Bujumbura"},
   "MK" {:coordinates "+4159+02126", :zone "Europe/Skopje"},
   "MU" {:coordinates "-2010+05730", :zone "Indian/Mauritius"},
   "LI" {:coordinates "+4709+00931", :zone "Europe/Vaduz"},
   "NU" {:coordinates "-1901-16955", :zone "Pacific/Niue"},
   "GR" {:coordinates "+3758+02343", :zone "Europe/Athens"},
   "GY" {:coordinates "+0648-05810", :zone "America/Guyana"},
   "CG" {:coordinates "-0416+01517", :zone "Africa/Brazzaville"},
   "NF" {:coordinates "-2903+16758", :zone "Pacific/Norfolk"},
   "ML" {:coordinates "+1239-00800", :zone "Africa/Bamako"},
   "AX" {:coordinates "+6006+01957", :zone "Europe/Mariehamn"},
   "GM" {:coordinates "+1328-01639", :zone "Africa/Banjul"},
   "SA" {:coordinates "+2438+04643", :zone "Asia/Riyadh"},
   "CX" {:coordinates "-1025+10543", :zone "Indian/Christmas"},
   "BH" {:coordinates "+2623+05035", :zone "Asia/Bahrain"},
   "NE" {:coordinates "+1331+00207", :zone "Africa/Niamey"},
   "BN" {:coordinates "+0456+11455", :zone "Asia/Brunei"},
   "MF" {:coordinates "+1804-06305", :zone "America/Marigot"},
   "CD" {:coordinates "-1140+02728", :zone "Africa/Lubumbashi"},
   "DK" {:coordinates "+5540+01235", :zone "Europe/Copenhagen"},
   "BJ" {:coordinates "+0629+00237", :zone "Africa/Porto-Novo"},
   "ME" {:coordinates "+4226+01916", :zone "Europe/Podgorica"},
   "SJ" {:coordinates "+7800+01600", :zone "Arctic/Longyearbyen"},
   "BO" {:coordinates "-1630-06809", :zone "America/La_Paz"},
   "JO" {:coordinates "+3157+03556", :zone "Asia/Amman"},
   "CV" {:coordinates "+1455-02331", :zone "Atlantic/Cape_Verde"},
   "VE" {:coordinates "+1030-06656", :zone "America/Caracas"},
   "CI" {:coordinates "+0519-00402", :zone "Africa/Abidjan"},
   "UZ" {:coordinates "+4120+06918", :zone "Asia/Tashkent"},
   "TN" {:coordinates "+3648+01011", :zone "Africa/Tunis"},
   "IS" {:coordinates "+6409-02151", :zone "Atlantic/Reykjavik"},
   "EH" {:coordinates "+2709-01312", :zone "Africa/El_Aaiun"},
   "TM" {:coordinates "+3757+05823", :zone "Asia/Ashgabat"},
   "GA" {:coordinates "+0023+00927", :zone "Africa/Libreville"},
   "LS" {:coordinates "-2928+02730", :zone "Africa/Maseru"},
   "TZ" {:coordinates "-0648+03917", :zone "Africa/Dar_es_Salaam"},
   "AT" {:coordinates "+4813+01620", :zone "Europe/Vienna"},
   "LT" {:coordinates "+5441+02519", :zone "Europe/Vilnius"},
   "NP" {:coordinates "+2743+08519", :zone "Asia/Kathmandu"},
   "BG" {:coordinates "+4241+02319", :zone "Europe/Sofia"},
   "IL" {:coordinates "+314650+0351326", :zone "Asia/Jerusalem"},
   "GU" {:coordinates "+1328+14445", :zone "Pacific/Guam"},
   "PK" {:coordinates "+2452+06703", :zone "Asia/Karachi"},
   "PT" {:coordinates "+3744-02540", :zone "Atlantic/Azores"},
   "HR" {:coordinates "+4548+01558", :zone "Europe/Zagreb"},
   "VU" {:coordinates "-1740+16825", :zone "Pacific/Efate"},
   "PF" {:coordinates "-2308-13457", :zone "Pacific/Gambier"},
   "BM" {:coordinates "+3217-06446", :zone "Atlantic/Bermuda"},
   "MR" {:coordinates "+1806-01557", :zone "Africa/Nouakchott"},
   "GE" {:coordinates "+4143+04449", :zone "Asia/Tbilisi"},
   "HU" {:coordinates "+4730+01905", :zone "Europe/Budapest"},
   "TW" {:coordinates "+2503+12130", :zone "Asia/Taipei"},
   "MM" {:coordinates "+1647+09610", :zone "Asia/Yangon"},
   "VG" {:coordinates "+1827-06437", :zone "America/Tortola"},
   "YE" {:coordinates "+1245+04512", :zone "Asia/Aden"},
   "SR" {:coordinates "+0550-05510", :zone "America/Paramaribo"},
   "PN" {:coordinates "-2504-13005", :zone "Pacific/Pitcairn"},
   "VA" {:coordinates "+415408+0122711", :zone "Europe/Vatican"},
   "PR" {:coordinates "+182806-0660622", :zone "America/Puerto_Rico"},
   "KW" {:coordinates "+2920+04759", :zone "Asia/Kuwait"},
   "SE" {:coordinates "+5920+01803", :zone "Europe/Stockholm"},
   "GB" {:coordinates "+513030-0000731", :zone "Europe/London"},
   "UM" {:coordinates "+1917+16637", :zone "Pacific/Wake"},
   "VN" {:coordinates "+1045+10640", :zone "Asia/Ho_Chi_Minh"},
   "CF" {:coordinates "+0422+01835", :zone "Africa/Bangui"},
   "PA" {:coordinates "+0858-07932", :zone "America/Panama"},
   "VC" {:coordinates "+1309-06114", :zone "America/St_Vincent"},
   "JP" {:coordinates "+353916+1394441", :zone "Asia/Tokyo"},
   "IR" {:coordinates "+3540+05126", :zone "Asia/Tehran"},
   "AF" {:coordinates "+3431+06912", :zone "Asia/Kabul"},
   "LY" {:coordinates "+3254+01311", :zone "Africa/Tripoli"},
   "MZ" {:coordinates "-2558+03235", :zone "Africa/Maputo"},
   "RO" {:coordinates "+4426+02606", :zone "Europe/Bucharest"},
   "QA" {:coordinates "+2517+05132", :zone "Asia/Qatar"},
   "CM" {:coordinates "+0403+00942", :zone "Africa/Douala"},
   "GG" {:coordinates "+492717-0023210", :zone "Europe/Guernsey"},
   "BY" {:coordinates "+5354+02734", :zone "Europe/Minsk"},
   "SD" {:coordinates "+1536+03232", :zone "Africa/Khartoum"},
   "BQ" {:coordinates "+120903-0681636", :zone "America/Kralendijk"},
   "MO" {:coordinates "+2214+11335", :zone "Asia/Macau"},
   "KY" {:coordinates "+1918-08123", :zone "America/Cayman"},
   "AR" {:coordinates "-5448-06818", :zone "America/Argentina/Ushuaia"},
   "BR" {:coordinates "-0958-06748", :zone "America/Rio_Branco"},
   "ZW" {:coordinates "-1750+03103", :zone "Africa/Harare"},
   "NR" {:coordinates "-0031+16655", :zone "Pacific/Nauru"},
   "NZ" {:coordinates "-4357-17633", :zone "Pacific/Chatham"},
   "AW" {:coordinates "+1230-06958", :zone "America/Aruba"},
   "FJ" {:coordinates "-1808+17825", :zone "Pacific/Fiji"},
   "ID" {:coordinates "-0232+14042", :zone "Asia/Jayapura"},
   "SV" {:coordinates "+1342-08912", :zone "America/El_Salvador"},
   "CN" {:coordinates "+4348+08735", :zone "Asia/Urumqi"},
   "FM" {:coordinates "+0519+16259", :zone "Pacific/Kosrae"},
   "HT" {:coordinates "+1832-07220", :zone "America/Port-au-Prince"},
   "CC" {:coordinates "-1210+09655", :zone "Indian/Cocos"},
   "RW" {:coordinates "-0157+03004", :zone "Africa/Kigali"},
   "BA" {:coordinates "+4352+01825", :zone "Europe/Sarajevo"},
   "TL" {:coordinates "-0833+12535", :zone "Asia/Dili"},
   "JM" {:coordinates "+175805-0764736", :zone "America/Jamaica"},
   "KM" {:coordinates "-1141+04316", :zone "Indian/Comoro"},
   "KE" {:coordinates "-0117+03649", :zone "Africa/Nairobi"},
   "WS" {:coordinates "-1350-17144", :zone "Pacific/Apia"},
   "TO" {:coordinates "-2110-17510", :zone "Pacific/Tongatapu"},
   "PY" {:coordinates "-2516-05740", :zone "America/Asuncion"},
   "SH" {:coordinates "-1555-00542", :zone "Atlantic/St_Helena"},
   "CY" {:coordinates "+3507+03357", :zone "Asia/Famagusta"},
   "GH" {:coordinates "+0533-00013", :zone "Africa/Accra"},
   "MA" {:coordinates "+3339-00735", :zone "Africa/Casablanca"},
   "SG" {:coordinates "+0117+10351", :zone "Asia/Singapore"},
   "LK" {:coordinates "+0656+07951", :zone "Asia/Colombo"},
   "PH" {:coordinates "+1435+12100", :zone "Asia/Manila"},
   "SM" {:coordinates "+4355+01228", :zone "Europe/San_Marino"},
   "WF" {:coordinates "-1318-17610", :zone "Pacific/Wallis"},
   "TR" {:coordinates "+4101+02858", :zone "Europe/Istanbul"},
   "PS" {:coordinates "+313200+0350542", :zone "Asia/Hebron"},
   "BZ" {:coordinates "+1730-08812", :zone "America/Belize"},
   "CU" {:coordinates "+2308-08222", :zone "America/Havana"},
   "TV" {:coordinates "-0831+17913", :zone "Pacific/Funafuti"},
   "AD" {:coordinates "+4230+00131", :zone "Europe/Andorra"},
   "SB" {:coordinates "-0932+16012", :zone "Pacific/Guadalcanal"},
   "DM" {:coordinates "+1518-06124", :zone "America/Dominica"},
   "LR" {:coordinates "+0618-01047", :zone "Africa/Monrovia"},
   "OM" {:coordinates "+2336+05835", :zone "Asia/Muscat"},
   "SO" {:coordinates "+0204+04522", :zone "Africa/Mogadishu"},
   "DO" {:coordinates "+1828-06954", :zone "America/Santo_Domingo"},
   "AL" {:coordinates "+4120+01950", :zone "Europe/Tirane"},
   "BL" {:coordinates "+1753-06251", :zone "America/St_Barthelemy"},
   "FR" {:coordinates "+4852+00220", :zone "Europe/Paris"},
   "GW" {:coordinates "+1151-01535", :zone "Africa/Bissau"},
   "MS" {:coordinates "+1643-06213", :zone "America/Montserrat"},
   "BB" {:coordinates "+1306-05937", :zone "America/Barbados"},
   "CA" {:coordinates "+6404-13925", :zone "America/Dawson"},
   "MG" {:coordinates "-1855+04731", :zone "Indian/Antananarivo"},
   "KH" {:coordinates "+1133+10455", :zone "Asia/Phnom_Penh"},
   "LA" {:coordinates "+1758+10236", :zone "Asia/Vientiane"},
   "GP" {:coordinates "+1614-06132", :zone "America/Guadeloupe"},
   "HN" {:coordinates "+1406-08713", :zone "America/Tegucigalpa"},
   "TH" {:coordinates "+1345+10031", :zone "Asia/Bangkok"},
   "DE" {:coordinates "+4742+00841", :zone "Europe/Busingen"},
   "LB" {:coordinates "+3353+03530", :zone "Asia/Beirut"},
   "KZ" {:coordinates "+5113+05121", :zone "Asia/Oral"},
   "AS" {:coordinates "-1416-17042", :zone "Pacific/Pago_Pago"},
   "EC" {:coordinates "-0054-08936", :zone "Pacific/Galapagos"},
   "NO" {:coordinates "+5955+01045", :zone "Europe/Oslo"},
   "AO" {:coordinates "-0848+01314", :zone "Africa/Luanda"},
   "FK" {:coordinates "-5142-05751", :zone "Atlantic/Stanley"},
   "ET" {:coordinates "+0902+03842", :zone "Africa/Addis_Ababa"},
   "GS" {:coordinates "-5416-03632", :zone "Atlantic/South_Georgia"},
   "MD" {:coordinates "+4700+02850", :zone "Europe/Chisinau"},
   "AG" {:coordinates "+1703-06148", :zone "America/Antigua"},
   "BE" {:coordinates "+5050+00420", :zone "Europe/Brussels"},
   "MV" {:coordinates "+0410+07330", :zone "Indian/Maldives"},
   "SZ" {:coordinates "-2618+03106", :zone "Africa/Mbabane"},
   "CZ" {:coordinates "+5005+01426", :zone "Europe/Prague"},
   "CL" {:coordinates "-2709-10926", :zone "Pacific/Easter"},
   "BT" {:coordinates "+2728+08939", :zone "Asia/Thimphu"},
   "NL" {:coordinates "+5222+00454", :zone "Europe/Amsterdam"},
   "EG" {:coordinates "+3003+03115", :zone "Africa/Cairo"},
   "MQ" {:coordinates "+1436-06105", :zone "America/Martinique"},
   "SN" {:coordinates "+1440-01726", :zone "Africa/Dakar"},
   "FO" {:coordinates "+6201-00646", :zone "Atlantic/Faroe"},
   "EE" {:coordinates "+5925+02445", :zone "Europe/Tallinn"},
   "AQ" {:coordinates "-7824+10654", :zone "Antarctica/Vostok"},
   "ST" {:coordinates "+0020+00644", :zone "Africa/Sao_Tome"},
   "KN" {:coordinates "+1718-06243", :zone "America/St_Kitts"},
   "BW" {:coordinates "-2439+02555", :zone "Africa/Gaborone"},
   "MH" {:coordinates "+0905+16720", :zone "Pacific/Kwajalein"},
   "NI" {:coordinates "+1209-08617", :zone "America/Managua"},
   "PG" {:coordinates "-0613+15534", :zone "Pacific/Bougainville"},
   "VI" {:coordinates "+1821-06456", :zone "America/St_Thomas"},
   "IQ" {:coordinates "+3321+04425", :zone "Asia/Baghdad"},
   "KG" {:coordinates "+4254+07436", :zone "Asia/Bishkek"},
   "US" {:coordinates "+211825-1575130", :zone "Pacific/Honolulu"},
   "ZM" {:coordinates "-1525+02817", :zone "Africa/Lusaka"},
   "MC" {:coordinates "+4342+00723", :zone "Europe/Monaco"},
   "GI" {:coordinates "+3608-00521", :zone "Europe/Gibraltar"},
   "NC" {:coordinates "-2216+16627", :zone "Pacific/Noumea"},
   "GT" {:coordinates "+1438-09031", :zone "America/Guatemala"},
   "BF" {:coordinates "+1222-00131", :zone "Africa/Ouagadougou"},
   "YT" {:coordinates "-1247+04514", :zone "Indian/Mayotte"},
   "LU" {:coordinates "+4936+00609", :zone "Europe/Luxembourg"},
   "UA" {:coordinates "+4750+03510", :zone "Europe/Zaporozhye"},
   "IE" {:coordinates "+5320-00615", :zone "Europe/Dublin"},
   "LV" {:coordinates "+5657+02406", :zone "Europe/Riga"},
   "GD" {:coordinates "+1203-06145", :zone "America/Grenada"},
   "MW" {:coordinates "-1547+03500", :zone "Africa/Blantyre"},
   "BS" {:coordinates "+2505-07721", :zone "America/Nassau"},
   "AZ" {:coordinates "+4023+04951", :zone "Asia/Baku"},
   "SK" {:coordinates "+4809+01707", :zone "Europe/Bratislava"},
   "GQ" {:coordinates "+0345+00847", :zone "Africa/Malabo"},
   "TC" {:coordinates "+2128-07108", :zone "America/Grand_Turk"},
   "RE" {:coordinates "-2052+05528", :zone "Indian/Reunion"},
   "IN" {:coordinates "+2232+08822", :zone "Asia/Kolkata"},
   "ES" {:coordinates "+2806-01524", :zone "Atlantic/Canary"},
   "GL" {:coordinates "+7634-06847", :zone "America/Thule"},
   "KI" {:coordinates "+0152-15720", :zone "Pacific/Kiritimati"},
   "HK" {:coordinates "+2217+11409", :zone "Asia/Hong_Kong"},
   "CO" {:coordinates "+0436-07405", :zone "America/Bogota"},
   "SS" {:coordinates "+0451+03137", :zone "Africa/Juba"},
   "RS" {:coordinates "+4450+02030", :zone "Europe/Belgrade"},
   "IO" {:coordinates "-0720+07225", :zone "Indian/Chagos"},
   "NG" {:coordinates "+0627+00324", :zone "Africa/Lagos"},
   "UG" {:coordinates "+0019+03225", :zone "Africa/Kampala"},
   "CW" {:coordinates "+1211-06900", :zone "America/Curacao"},
   "SL" {:coordinates "+0830-01315", :zone "Africa/Freetown"},
   "ER" {:coordinates "+1520+03853", :zone "Africa/Asmara"},
   "JE" {:coordinates "+491101-0020624", :zone "Europe/Jersey"},
   "AE" {:coordinates "+2518+05518", :zone "Asia/Dubai"},
   "PM" {:coordinates "+4703-05620", :zone "America/Miquelon"},
   "BD" {:coordinates "+2343+09025", :zone "Asia/Dhaka"},
   "MT" {:coordinates "+3554+01431", :zone "Europe/Malta"},
   "AI" {:coordinates "+1812-06304", :zone "America/Anguilla"},
   "GN" {:coordinates "+0931-01343", :zone "Africa/Conakry"},
   "PW" {:coordinates "+0720+13429", :zone "Pacific/Palau"},
   "NA" {:coordinates "-2234+01706", :zone "Africa/Windhoek"},
   "MX" {:coordinates "+2048-10515", :zone "America/Bahia_Banderas"},
   "PL" {:coordinates "+5215+02100", :zone "Europe/Warsaw"}})


(def timezone->locale
  (reduce 
    (fn [result [locale {:keys [zone]}]]
      (assoc result zone locale))
    nil
    locales))

(def db
  {:zones
   {"Europe/Amsterdam"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 220924800000},
     :history
     [{:offset 1172000, :rule "-", :format "LMT", :until -4260211200000}
      {:offset 1172000,
       :rule "Neth",
       :format "%s",
       :until -1025740800000}
      {:offset 1200000,
       :rule "Neth",
       :format "+0020/+0120",
       :until -935020800000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -781048800000}
      {:offset 3600000,
       :rule "Neth",
       :format "CE%sT",
       :until 220924800000}]},
    "America/Montevideo"
    {:current
     {:offset -10800000,
      :rule "Uruguay",
      :format "-03/-02",
      :from 156902400000},
     :history
     [{:offset -13491000,
       :rule "-",
       :format "LMT",
       :until -1942704000000}
      {:offset -13491000,
       :rule "-",
       :format "MMT",
       :until -1567468800000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1459641600000}
      {:offset -12600000,
       :rule "Uruguay",
       :format "-0330/-03",
       :until -853632000000}
      {:offset -10800000,
       :rule "Uruguay",
       :format "-03/-0230",
       :until -315619200000}
      {:offset -10800000,
       :rule "Uruguay",
       :format "-03/-02",
       :until -63158400000}
      {:offset -10800000, :rule "Uruguay", :format "-03/-0230", :until 0}
      {:offset -10800000,
       :rule "Uruguay",
       :format "-03/-02",
       :until 126230400000}
      {:offset -10800000,
       :rule "Uruguay",
       :format "-03/-0130",
       :until 132105600000}
      {:offset -10800000,
       :rule "Uruguay",
       :format "-03/-0230",
       :until 156902400000}]},
    "Africa/Kinshasa" "Africa/Lagos",
    "Asia/Sakhalin"
    {:current
     {:offset 39600000, :rule "-", :format "+11", :from 1459090800000},
     :history
     [{:offset 34248000, :rule "-", :format "LMT", :until -2031004800000}
      {:offset 32400000, :rule "-", :format "+09", :until -768528000000}
      {:offset 39600000,
       :rule "Russia",
       :format "+11/+12",
       :until 670424400000}
      {:offset 36000000,
       :rule "Russia",
       :format "+10/+11",
       :until 695829600000}
      {:offset 39600000,
       :rule "Russia",
       :format "+11/+12",
       :until 859723200000}
      {:offset 36000000,
       :rule "Russia",
       :format "+10/+11",
       :until 1301238000000}
      {:offset 39600000, :rule "-", :format "+11", :until 1414328400000}
      {:offset 36000000,
       :rule "-",
       :format "+10",
       :until 1459090800000}]},
    "America/Santarem"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1214265600000},
     :history
     [{:offset -13128000,
       :rule "-",
       :format "LMT",
       :until -1767225600000}
      {:offset -14400000,
       :rule "Brazil",
       :format "-04/-03",
       :until 590025600000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until 1214265600000}]},
    "Africa/Ouagadougou" "Africa/Abidjan",
    "Asia/Amman"
    {:current
     {:offset 7200000,
      :rule "Jordan",
      :format "EE%sT",
      :from -1230768000000},
     :history
     [{:offset 8624000,
       :rule "-",
       :format "LMT",
       :until -1230768000000}]},
    "Europe/Tallinn"
    {:current
     {:offset 7200000, :rule "EU", :format "EE%sT", :from 1014249600000},
     :history
     [{:offset 5940000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 5940000, :rule "-", :format "TMT", :until -1638316800000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -1593820800000}
      {:offset 5940000, :rule "-", :format "TMT", :until -1535932800000}
      {:offset 7200000, :rule "-", :format "EET", :until -927936000000}
      {:offset 10800000, :rule "-", :format "MSK", :until -892944000000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -797644800000}
      {:offset 10800000,
       :rule "Russia",
       :format "MSK/MSD",
       :until 606891600000}
      {:offset 7200000,
       :rule 3600000,
       :format "EEST",
       :until 622620000000}
      {:offset 7200000,
       :rule "C-Eur",
       :format "EE%sT",
       :until 906422400000}
      {:offset 7200000, :rule "EU", :format "EE%sT", :until 941342400000}
      {:offset 7200000,
       :rule "-",
       :format "EET",
       :until 1014249600000}]},
    "Africa/Blantyre" "Africa/Maputo",
    "America/Nipigon"
    {:current
     {:offset -18000000,
      :rule "Canada",
      :format "E%sT",
      :from -880246800000},
     :history
     [{:offset -21184000,
       :rule "-",
       :format "LMT",
       :until -2366755200000}
      {:offset -18000000,
       :rule "Canada",
       :format "E%sT",
       :until -923270400000}
      {:offset -18000000,
       :rule 3600000,
       :format "EDT",
       :until -880246800000}]},
    "America/Jamaica"
    {:current
     {:offset -18000000, :rule "-", :format "EST", :from 441763200000},
     :history
     [{:offset -18430000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -18430000,
       :rule "-",
       :format "KMT",
       :until -1827705600000}
      {:offset -18000000, :rule "-", :format "EST", :until 126230400000}
      {:offset -18000000,
       :rule "US",
       :format "E%sT",
       :until 441763200000}]},
    "Africa/El_Aaiun"
    {:current
     {:offset 0, :rule "Morocco", :format "WE%sT", :from 198288000000},
     :history
     [{:offset -3168000, :rule "-", :format "LMT", :until -1136073600000}
      {:offset -3600000,
       :rule "-",
       :format "-01",
       :until 198288000000}]},
    "America/Costa_Rica"
    {:current
     {:offset -21600000,
      :rule "CR",
      :format "C%sT",
      :from -1545091200000},
     :history
     [{:offset -20173000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -20173000,
       :rule "-",
       :format "SJMT",
       :until -1545091200000}]},
    "Asia/Kuala_Lumpur"
    {:current
     {:offset 28800000, :rule "-", :format "+08", :from 378691200000},
     :history
     [{:offset 24406000, :rule "-", :format "LMT", :until -2177452800000}
      {:offset 24925000, :rule "-", :format "SMT", :until -2038176000000}
      {:offset 25200000, :rule "-", :format "+07", :until -1167609600000}
      {:offset 25200000,
       :rule 1200000,
       :format "+0720",
       :until -1073001600000}
      {:offset 26400000,
       :rule "-",
       :format "+0720",
       :until -894153600000}
      {:offset 27000000,
       :rule "-",
       :format "+0730",
       :until -879638400000}
      {:offset 32400000, :rule "-", :format "+09", :until -766972800000}
      {:offset 27000000,
       :rule "-",
       :format "+0730",
       :until 378691200000}]},
    "Asia/Kuwait" "Asia/Riyadh",
    "Asia/Tokyo"
    {:current
     {:offset 32400000,
      :rule "Japan",
      :format "J%sT",
      :from -2587766400000},
     :history
     [{:offset 33539000,
       :rule "-",
       :format "LMT",
       :until -2587766400000}]},
    "America/Maceio"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1033430400000},
     :history
     [{:offset -8572000, :rule "-", :format "LMT", :until -1767225600000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 653529600000}
      {:offset -10800000, :rule "-", :format "-03", :until 813542400000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 841795200000}
      {:offset -10800000, :rule "-", :format "-03", :until 938649600000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 972172800000}
      {:offset -10800000, :rule "-", :format "-03", :until 1000339200000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 1033430400000}]},
    "Europe/Brussels"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 220924800000},
     :history
     [{:offset 1050000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 1050000, :rule "-", :format "BMT", :until -2450952000000}
      {:offset 0, :rule "-", :format "WET", :until -1740355200000}
      {:offset 3600000, :rule "-", :format "CET", :until -1693699200000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -1613822400000}
      {:offset 0, :rule "Belgium", :format "WE%sT", :until -934660800000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -799286400000}
      {:offset 3600000,
       :rule "Belgium",
       :format "CE%sT",
       :until 220924800000}]},
    "America/Nome"
    {:current
     {:offset -32400000,
      :rule "US",
      :format "AK%sT",
      :from 438998400000},
     :history
     [{:offset 46702000, :rule "-", :format "LMT", :until -3225225600000}
      {:offset -39698000,
       :rule "-",
       :format "LMT",
       :until -2188987200000}
      {:offset -39600000, :rule "-", :format "NST", :until -883612800000}
      {:offset -39600000,
       :rule "US",
       :format "N%sT",
       :until -757382400000}
      {:offset -39600000, :rule "-", :format "NST", :until -86918400000}
      {:offset -39600000, :rule "-", :format "BST", :until -31536000000}
      {:offset -39600000,
       :rule "US",
       :format "B%sT",
       :until 436327200000}
      {:offset -32400000,
       :rule "US",
       :format "Y%sT",
       :until 438998400000}]},
    "Australia/Broken_Hill"
    {:current
     {:offset 34200000, :rule "AS", :format "AC%sT", :from 946684800000},
     :history
     [{:offset 33948000, :rule "-", :format "LMT", :until -2364076800000}
      {:offset 36000000,
       :rule "-",
       :format "AEST",
       :until -2314915200000}
      {:offset 32400000,
       :rule "-",
       :format "ACST",
       :until -2230156800000}
      {:offset 34200000,
       :rule "Aus",
       :format "AC%sT",
       :until 31536000000}
      {:offset 34200000,
       :rule "AN",
       :format "AC%sT",
       :until 946684800000}]},
    "Asia/Chita"
    {:current
     {:offset 32400000, :rule "-", :format "+09", :from 1459040400000},
     :history
     [{:offset 27232000, :rule "-", :format "LMT", :until -1579392000000}
      {:offset 28800000, :rule "-", :format "+08", :until -1247529600000}
      {:offset 32400000,
       :rule "Russia",
       :format "+09/+10",
       :until 670420800000}
      {:offset 28800000,
       :rule "Russia",
       :format "+08/+09",
       :until 695822400000}
      {:offset 32400000,
       :rule "Russia",
       :format "+09/+10",
       :until 1301227200000}
      {:offset 36000000, :rule "-", :format "+10", :until 1414324800000}
      {:offset 28800000,
       :rule "-",
       :format "+08",
       :until 1459040400000}]},
    "Africa/Casablanca"
    {:current
     {:offset 0, :rule "Morocco", :format "WE%sT", :from 504921600000},
     :history
     [{:offset -1820000, :rule "-", :format "LMT", :until -1773014400000}
      {:offset 0, :rule "Morocco", :format "WE%sT", :until 448243200000}
      {:offset 3600000, :rule "-", :format "CET", :until 504921600000}]},
    "Europe/Isle_of_Man" "Europe/London",
    "America/Argentina/San_Luis"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1255219200000},
     :history
     [{:offset -15924000,
       :rule "-",
       :format "LMT",
       :until -2372112000000}
      {:offset -15408000,
       :rule "-",
       :format "CMT",
       :until -1567468800000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1233446400000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until -7603200000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 631152000000}
      {:offset -10800000,
       :rule 3600000,
       :format "-02",
       :until 637372800000}
      {:offset -14400000, :rule "-", :format "-04", :until 655948800000}
      {:offset -14400000,
       :rule 3600000,
       :format "-03",
       :until 667785600000}
      {:offset -14400000, :rule "-", :format "-04", :until 675734400000}
      {:offset -10800000, :rule "-", :format "-03", :until 938908800000}
      {:offset -14400000,
       :rule 3600000,
       :format "-03",
       :until 952041600000}
      {:offset -10800000, :rule "-", :format "-03", :until 1085961600000}
      {:offset -14400000, :rule "-", :format "-04", :until 1090713600000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 1200873600000}
      {:offset -14400000,
       :rule "SanLuis",
       :format "-04/-03",
       :until 1255219200000}]},
    "America/St_Johns"
    {:current
     {:offset -12600000,
      :rule "Canada",
      :format "N%sT",
      :from 1320105600000},
     :history
     [{:offset -12652000,
       :rule "-",
       :format "LMT",
       :until -2713910400000}
      {:offset -12652000,
       :rule "StJohns",
       :format "N%sT",
       :until -1640995200000}
      {:offset -12652000,
       :rule "Canada",
       :format "N%sT",
       :until -1609459200000}
      {:offset -12652000,
       :rule "StJohns",
       :format "N%sT",
       :until -1096934400000}
      {:offset -12600000,
       :rule "StJohns",
       :format "N%sT",
       :until -872380800000}
      {:offset -12600000,
       :rule "Canada",
       :format "N%sT",
       :until -757382400000}
      {:offset -12600000,
       :rule "StJohns",
       :format "N%sT",
       :until 1320105600000}]},
    "Europe/Berlin"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 315532800000},
     :history
     [{:offset 3208000, :rule "-", :format "LMT", :until -2422051200000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -776556000000}
      {:offset 3600000,
       :rule "SovietZone",
       :format "CE%sT",
       :until -757382400000}
      {:offset 3600000,
       :rule "Germany",
       :format "CE%sT",
       :until 315532800000}]},
    "Africa/Lome" "Africa/Abidjan",
    "America/North_Dakota/Center"
    {:current
     {:offset -21600000, :rule "US", :format "C%sT", :from 719978400000},
     :history
     [{:offset -24312000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -25200000,
       :rule "US",
       :format "M%sT",
       :until 719978400000}]},
    "Asia/Aqtobe"
    {:current
     {:offset 18000000, :rule "-", :format "+05", :from 1099206000000},
     :history
     [{:offset 13720000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 14400000, :rule "-", :format "+04", :until -1247529600000}
      {:offset 18000000, :rule "-", :format "+05", :until 354931200000}
      {:offset 18000000,
       :rule 3600000,
       :format "+06",
       :until 370742400000}
      {:offset 21600000, :rule "-", :format "+06", :until 386467200000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 670413600000}
      {:offset 14400000,
       :rule "RussiaAsia",
       :format "+04/+05",
       :until 695808000000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 1099206000000}]},
    "Antarctica/Macquarie"
    {:current
     {:offset 39600000, :rule "-", :format "+11", :from 1270350000000},
     :history
     [{:offset 0, :rule "-", :format "-00", :until -2214259200000}
      {:offset 36000000,
       :rule "-",
       :format "AEST",
       :until -1680472800000}
      {:offset 36000000,
       :rule 3600000,
       :format "AEDT",
       :until -1669852800000}
      {:offset 36000000,
       :rule "Aus",
       :format "AE%sT",
       :until -1601643600000}
      {:offset 0, :rule "-", :format "-00", :until -687052800000}
      {:offset 36000000,
       :rule "Aus",
       :format "AE%sT",
       :until -94694400000}
      {:offset 36000000,
       :rule "AT",
       :format "AE%sT",
       :until 1270350000000}]},
    "Asia/Seoul"
    {:current
     {:offset 32400000,
      :rule "ROK",
      :format "K%sT",
      :from -264902400000},
     :history
     [{:offset 30472000, :rule "-", :format "LMT", :until -1948752000000}
      {:offset 30600000, :rule "-", :format "KST", :until -1830384000000}
      {:offset 32400000, :rule "-", :format "JST", :until -767318400000}
      {:offset 32400000, :rule "-", :format "KST", :until -498096000000}
      {:offset 30600000,
       :rule "ROK",
       :format "K%sT",
       :until -264902400000}]},
    "America/Punta_Arenas"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1480809600000},
     :history
     [{:offset -17020000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -16966000,
       :rule "-",
       :format "SMT",
       :until -1892678400000}
      {:offset -18000000,
       :rule "-",
       :format "-05",
       :until -1688428800000}
      {:offset -16966000,
       :rule "-",
       :format "SMT",
       :until -1619222400000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1593820800000}
      {:offset -16966000,
       :rule "-",
       :format "SMT",
       :until -1336003200000}
      {:offset -18000000,
       :rule "Chile",
       :format "-05/-04",
       :until -1178150400000}
      {:offset -14400000, :rule "-", :format "-04", :until -870566400000}
      {:offset -18000000, :rule "-", :format "-05", :until -865296000000}
      {:offset -14400000, :rule "-", :format "-04", :until -718070400000}
      {:offset -18000000, :rule "-", :format "-05", :until -713667600000}
      {:offset -14400000,
       :rule "Chile",
       :format "-04/-03",
       :until 1480809600000}]},
    "Indian/Christmas"
    {:current
     {:offset 25200000, :rule "-", :format "+07", :from -2364076800000},
     :history
     [{:offset 25372000,
       :rule "-",
       :format "LMT",
       :until -2364076800000}]},
    "Atlantic/Madeira"
    {:current
     {:offset 0, :rule "EU", :format "WE%sT", :from 433303200000},
     :history
     [{:offset -4056000, :rule "-", :format "LMT", :until -2713910400000}
      {:offset -4056000, :rule "-", :format "FMT", :until -1830376800000}
      {:offset -3600000,
       :rule "Port",
       :format "-01/+00",
       :until -873680856000}
      {:offset -3600000,
       :rule "Port",
       :format "+01",
       :until -864003600000}
      {:offset -3600000,
       :rule "Port",
       :format "-01/+00",
       :until -842835600000}
      {:offset -3600000,
       :rule "Port",
       :format "+01",
       :until -831344400000}
      {:offset -3600000,
       :rule "Port",
       :format "-01/+00",
       :until -810781200000}
      {:offset -3600000,
       :rule "Port",
       :format "+01",
       :until -799894800000}
      {:offset -3600000,
       :rule "Port",
       :format "-01/+00",
       :until -779335200000}
      {:offset -3600000,
       :rule "Port",
       :format "+01",
       :until -768445200000}
      {:offset -3600000,
       :rule "Port",
       :format "-01/+00",
       :until -118274400000}
      {:offset 0, :rule "Port", :format "WE%sT", :until 433303200000}]},
    "Asia/Barnaul"
    {:current
     {:offset 25200000, :rule "-", :format "+07", :from 1459076400000},
     :history
     [{:offset 20100000, :rule "-", :format "LMT", :until -1579824000000}
      {:offset 21600000, :rule "-", :format "+06", :until -1247529600000}
      {:offset 25200000,
       :rule "Russia",
       :format "+07/+08",
       :until 670413600000}
      {:offset 21600000,
       :rule "Russia",
       :format "+06/+07",
       :until 695815200000}
      {:offset 25200000,
       :rule "Russia",
       :format "+07/+08",
       :until 801619200000}
      {:offset 21600000,
       :rule "Russia",
       :format "+06/+07",
       :until 1301223600000}
      {:offset 25200000, :rule "-", :format "+07", :until 1414314000000}
      {:offset 21600000,
       :rule "-",
       :format "+06",
       :until 1459076400000}]},
    "Asia/Colombo"
    {:current
     {:offset 19800000, :rule "-", :format "+0530", :from 1145061000000},
     :history
     [{:offset 19164000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 19172000, :rule "-", :format "MMT", :until -2019686400000}
      {:offset 19800000,
       :rule "-",
       :format "+0530",
       :until -883267200000}
      {:offset 19800000,
       :rule 1800000,
       :format "+06",
       :until -862617600000}
      {:offset 19800000,
       :rule 3600000,
       :format "+0630",
       :until -764028000000}
      {:offset 19800000, :rule "-", :format "+0530", :until 832982400000}
      {:offset 23400000, :rule "-", :format "+0630", :until 846289800000}
      {:offset 21600000,
       :rule "-",
       :format "+06",
       :until 1145061000000}]},
    "America/Argentina/Mendoza"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
     :history
     [{:offset -16516000,
       :rule "-",
       :format "LMT",
       :until -2372112000000}
      {:offset -15408000,
       :rule "-",
       :format "CMT",
       :until -1567468800000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1233446400000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until -7603200000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 636508800000}
      {:offset -14400000, :rule "-", :format "-04", :until 655948800000}
      {:offset -14400000,
       :rule 3600000,
       :format "-03",
       :until 667785600000}
      {:offset -14400000, :rule "-", :format "-04", :until 687484800000}
      {:offset -14400000,
       :rule 3600000,
       :format "-03",
       :until 699408000000}
      {:offset -14400000, :rule "-", :format "-04", :until 719366400000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 938908800000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until 952041600000}
      {:offset -10800000, :rule "-", :format "-03", :until 1085270400000}
      {:offset -14400000, :rule "-", :format "-04", :until 1096156800000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 1224288000000}]},
    "Asia/Novokuznetsk"
    {:current
     {:offset 25200000, :rule "-", :format "+07", :from 1301223600000},
     :history
     [{:offset 20928000, :rule "-", :format "LMT", :until -1441238400000}
      {:offset 21600000, :rule "-", :format "+06", :until -1247529600000}
      {:offset 25200000,
       :rule "Russia",
       :format "+07/+08",
       :until 670413600000}
      {:offset 21600000,
       :rule "Russia",
       :format "+06/+07",
       :until 695815200000}
      {:offset 25200000,
       :rule "Russia",
       :format "+07/+08",
       :until 1269770400000}
      {:offset 21600000,
       :rule "Russia",
       :format "+06/+07",
       :until 1301223600000}]},
    "America/Indiana/Vevay"
    {:current
     {:offset -18000000,
      :rule "US",
      :format "E%sT",
      :from 1136073600000},
     :history
     [{:offset -20416000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -495064800000}
      {:offset -18000000, :rule "-", :format "EST", :until -31536000000}
      {:offset -18000000, :rule "US", :format "E%sT", :until 94694400000}
      {:offset -18000000,
       :rule "-",
       :format "EST",
       :until 1136073600000}]},
    "America/Argentina/Rio_Gallegos"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
     :history
     [{:offset -16612000,
       :rule "-",
       :format "LMT",
       :until -2372112000000}
      {:offset -15408000,
       :rule "-",
       :format "CMT",
       :until -1567468800000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1233446400000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until -7603200000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 938908800000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until 952041600000}
      {:offset -10800000, :rule "-", :format "-03", :until 1086048000000}
      {:offset -14400000, :rule "-", :format "-04", :until 1087689600000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 1224288000000}]},
    "America/Fortaleza"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1033430400000},
     :history
     [{:offset -9240000, :rule "-", :format "LMT", :until -1767225600000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 653529600000}
      {:offset -10800000, :rule "-", :format "-03", :until 938649600000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 972172800000}
      {:offset -10800000, :rule "-", :format "-03", :until 1000339200000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 1033430400000}]},
    "Europe/Uzhgorod"
    {:current
     {:offset 7200000, :rule "EU", :format "EE%sT", :from 788918400000},
     :history
     [{:offset 5352000, :rule "-", :format "LMT", :until -2500934400000}
      {:offset 3600000, :rule "-", :format "CET", :until -946771200000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -796867200000}
      {:offset 3600000,
       :rule 3600000,
       :format "CEST",
       :until -794707200000}
      {:offset 3600000, :rule "-", :format "CET", :until -773452800000}
      {:offset 10800000,
       :rule "Russia",
       :format "MSK/MSD",
       :until 631152000000}
      {:offset 10800000, :rule "-", :format "MSK", :until 646797600000}
      {:offset 3600000, :rule "-", :format "CET", :until 670388400000}
      {:offset 7200000, :rule "-", :format "EET", :until 694224000000}
      {:offset 7200000,
       :rule "E-Eur",
       :format "EE%sT",
       :until 788918400000}]},
    "Asia/Beirut"
    {:current
     {:offset 7200000,
      :rule "Lebanon",
      :format "EE%sT",
      :from -2840140800000},
     :history
     [{:offset 8520000,
       :rule "-",
       :format "LMT",
       :until -2840140800000}]},
    "Africa/Sao_Tome"
    {:current
     {:offset 3600000, :rule "-", :format "WAT", :from 1514768400000},
     :history
     [{:offset 1616000, :rule "-", :format "LMT", :until -2713910400000}
      {:offset -2205000, :rule "-", :format "LMT", :until -1830380400000}
      {:offset 0, :rule "-", :format "GMT", :until 1514768400000}]},
    "Asia/Dubai"
    {:current
     {:offset 14400000, :rule "-", :format "+04", :from -1577923200000},
     :history
     [{:offset 13272000,
       :rule "-",
       :format "LMT",
       :until -1577923200000}]},
    "Europe/Stockholm"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 315532800000},
     :history
     [{:offset 4332000, :rule "-", :format "LMT", :until -2871676800000}
      {:offset 3614000, :rule "-", :format "SET", :until -2208988800000}
      {:offset 3600000, :rule "-", :format "CET", :until -1692493200000}
      {:offset 3600000,
       :rule 3600000,
       :format "CEST",
       :until -1680476400000}
      {:offset 3600000, :rule "-", :format "CET", :until 315532800000}]},
    "America/Boise"
    {:current
     {:offset -25200000, :rule "US", :format "M%sT", :from 129088800000},
     :history
     [{:offset -27889000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -28800000,
       :rule "US",
       :format "P%sT",
       :until -1471816800000}
      {:offset -25200000,
       :rule "US",
       :format "M%sT",
       :until 126230400000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until 129088800000}]},
    "America/Scoresbysund"
    {:current
     {:offset -3600000,
      :rule "EU",
      :format "-01/+00",
      :from 354672000000},
     :history
     [{:offset -5272000, :rule "-", :format "LMT", :until -1686096000000}
      {:offset -7200000, :rule "-", :format "-02", :until 323834400000}
      {:offset -7200000,
       :rule "C-Eur",
       :format "-02/-01",
       :until 354672000000}]},
    "Indian/Maldives"
    {:current
     {:offset 18000000, :rule "-", :format "+05", :from -315619200000},
     :history
     [{:offset 17640000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 17640000,
       :rule "-",
       :format "MMT",
       :until -315619200000}]},
    "Europe/Simferopol"
    {:current
     {:offset 10800000, :rule "-", :format "MSK", :from 1414299600000},
     :history
     [{:offset 8184000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 8160000, :rule "-", :format "SMT", :until -1441152000000}
      {:offset 7200000, :rule "-", :format "EET", :until -1247529600000}
      {:offset 10800000, :rule "-", :format "MSK", :until -888883200000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -811641600000}
      {:offset 10800000,
       :rule "Russia",
       :format "MSK/MSD",
       :until 631152000000}
      {:offset 10800000, :rule "-", :format "MSK", :until 646797600000}
      {:offset 7200000, :rule "-", :format "EET", :until 694224000000}
      {:offset 7200000,
       :rule "E-Eur",
       :format "EE%sT",
       :until 767750400000}
      {:offset 10800000,
       :rule "E-Eur",
       :format "MSK/MSD",
       :until 828244800000}
      {:offset 10800000,
       :rule 3600000,
       :format "MSD",
       :until 846399600000}
      {:offset 10800000,
       :rule "Russia",
       :format "MSK/MSD",
       :until 852076800000}
      {:offset 10800000, :rule "-", :format "MSK", :until 859683600000}
      {:offset 7200000,
       :rule "EU",
       :format "EE%sT",
       :until 1396141200000}
      {:offset 14400000,
       :rule "-",
       :format "MSK",
       :until 1414299600000}]},
    "Pacific/Efate"
    {:current
     {:offset 39600000,
      :rule "Vanuatu",
      :format "+11/+12",
      :from -1829347200000},
     :history
     [{:offset 40396000,
       :rule "-",
       :format "LMT",
       :until -1829347200000}]},
    "Asia/Qatar"
    {:current
     {:offset 10800000, :rule "-", :format "+03", :from 76204800000},
     :history
     [{:offset 12368000, :rule "-", :format "LMT", :until -1577923200000}
      {:offset 14400000, :rule "-", :format "+04", :until 76204800000}]},
    "Europe/Jersey" "Europe/London",
    "Pacific/Honolulu"
    {:current
     {:offset -36000000, :rule "-", :format "HST", :from -712188000000},
     :history
     [{:offset -37886000,
       :rule "-",
       :format "LMT",
       :until -2334182400000}
      {:offset -37800000,
       :rule "-",
       :format "HST",
       :until -1157320800000}
      {:offset -37800000,
       :rule 3600000,
       :format "HDT",
       :until -1155470400000}
      {:offset -37800000, :rule "-", :format "HST", :until -880236000000}
      {:offset -37800000,
       :rule 3600000,
       :format "HDT",
       :until -765410400000}
      {:offset -37800000,
       :rule "-",
       :format "HST",
       :until -712188000000}]},
    "America/Tegucigalpa"
    {:current
     {:offset -21600000,
      :rule "Hond",
      :format "C%sT",
      :from -1538524800000},
     :history
     [{:offset -20932000,
       :rule "-",
       :format "LMT",
       :until -1538524800000}]},
    "Atlantic/South_Georgia"
    {:current
     {:offset -7200000, :rule "-", :format "-02", :from -2524521600000},
     :history
     [{:offset -8768000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}]},
    "Asia/Hong_Kong"
    {:current
     {:offset 28800000,
      :rule "HK",
      :format "HK%sT",
      :from -766713600000},
     :history
     [{:offset 27402000, :rule "-", :format "LMT", :until -2056665600000}
      {:offset 28800000,
       :rule "HK",
       :format "HK%sT",
       :until -884217600000}
      {:offset 32400000,
       :rule "-",
       :format "JST",
       :until -766713600000}]},
    "Pacific/Pohnpei"
    {:current
     {:offset 39600000, :rule "-", :format "+11", :from -2177452800000},
     :history
     [{:offset 37972000,
       :rule "-",
       :format "LMT",
       :until -2177452800000}]},
    "Pacific/Norfolk"
    {:current
     {:offset 39600000, :rule "-", :format "+11", :from 1443924000000},
     :history
     [{:offset 40312000, :rule "-", :format "LMT", :until -2177452800000}
      {:offset 40320000,
       :rule "-",
       :format "+1112",
       :until -599616000000}
      {:offset 41400000, :rule "-", :format "+1130", :until 152071200000}
      {:offset 41400000,
       :rule 3600000,
       :format "+1230",
       :until 162957600000}
      {:offset 41400000,
       :rule "-",
       :format "+1130",
       :until 1443924000000}]},
    "America/Santiago"
    {:current
     {:offset -14400000,
      :rule "Chile",
      :format "-04/-03",
      :from -713667600000},
     :history
     [{:offset -16966000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -16966000,
       :rule "-",
       :format "SMT",
       :until -1892678400000}
      {:offset -18000000,
       :rule "-",
       :format "-05",
       :until -1688428800000}
      {:offset -16966000,
       :rule "-",
       :format "SMT",
       :until -1619222400000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1593820800000}
      {:offset -16966000,
       :rule "-",
       :format "SMT",
       :until -1336003200000}
      {:offset -18000000,
       :rule "Chile",
       :format "-05/-04",
       :until -1178150400000}
      {:offset -14400000, :rule "-", :format "-04", :until -870566400000}
      {:offset -18000000, :rule "-", :format "-05", :until -865296000000}
      {:offset -14400000, :rule "-", :format "-04", :until -740534400000}
      {:offset -14400000,
       :rule 3600000,
       :format "-03",
       :until -736387200000}
      {:offset -14400000, :rule "-", :format "-04", :until -718070400000}
      {:offset -18000000,
       :rule "-",
       :format "-05",
       :until -713667600000}]},
    "America/Indiana/Knox"
    {:current
     {:offset -21600000,
      :rule "US",
      :format "C%sT",
      :from 1143943200000},
     :history
     [{:offset -20790000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -725846400000}
      {:offset -21600000,
       :rule "Starke",
       :format "C%sT",
       :until -242258400000}
      {:offset -18000000, :rule "-", :format "EST", :until -195084000000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until 688528800000}
      {:offset -18000000,
       :rule "-",
       :format "EST",
       :until 1143943200000}]},
    "Australia/Brisbane"
    {:current
     {:offset 36000000, :rule "AQ", :format "AE%sT", :from 31536000000},
     :history
     [{:offset 36728000, :rule "-", :format "LMT", :until -2366755200000}
      {:offset 36000000,
       :rule "Aus",
       :format "AE%sT",
       :until 31536000000}]},
    "America/Anguilla" "America/Port_of_Spain",
    "Asia/Hovd"
    {:current
     {:offset 25200000,
      :rule "Mongol",
      :format "+07/+08",
      :from 252460800000},
     :history
     [{:offset 21996000, :rule "-", :format "LMT", :until -2032905600000}
      {:offset 21600000,
       :rule "-",
       :format "+06",
       :until 252460800000}]},
    "Asia/Jakarta"
    {:current
     {:offset 25200000, :rule "-", :format "WIB", :from -189388800000},
     :history
     [{:offset 25632000, :rule "-", :format "LMT", :until -3231273600000}
      {:offset 25632000, :rule "-", :format "BMT", :until -1451693580000}
      {:offset 26400000,
       :rule "-",
       :format "+0720",
       :until -1172880000000}
      {:offset 27000000,
       :rule "-",
       :format "+0730",
       :until -876614400000}
      {:offset 32400000, :rule "-", :format "+09", :until -766022400000}
      {:offset 27000000,
       :rule "-",
       :format "+0730",
       :until -683856000000}
      {:offset 28800000, :rule "-", :format "+08", :until -620784000000}
      {:offset 27000000,
       :rule "-",
       :format "+0730",
       :until -189388800000}]},
    "Atlantic/Stanley"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1283652000000},
     :history
     [{:offset -13884000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -13884000,
       :rule "-",
       :format "SMT",
       :until -1824249600000}
      {:offset -14400000,
       :rule "Falk",
       :format "-04/-03",
       :until 420595200000}
      {:offset -10800000,
       :rule "Falk",
       :format "-03/-02",
       :until 495590400000}
      {:offset -14400000,
       :rule "Falk",
       :format "-04/-03",
       :until 1283652000000}]},
    "Indian/Comoro" "Africa/Nairobi",
    "America/Sao_Paulo"
    {:current
     {:offset -10800000,
      :rule "Brazil",
      :format "-03/-02",
      :from -189388800000},
     :history
     [{:offset -11188000,
       :rule "-",
       :format "LMT",
       :until -1767225600000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until -195436800000}
      {:offset -10800000,
       :rule 3600000,
       :format "-02",
       :until -189388800000}]},
    "Asia/Macau"
    {:current
     {:offset 28800000,
      :rule "Macau",
      :format "C%sT",
      :from -1830470400000},
     :history
     [{:offset 27260000,
       :rule "-",
       :format "LMT",
       :until -1830470400000}]},
    "America/Menominee"
    {:current
     {:offset -21600000, :rule "US", :format "C%sT", :from 104896800000},
     :history
     [{:offset -21027000,
       :rule "-",
       :format "LMT",
       :until -2659824000000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -757382400000}
      {:offset -21600000,
       :rule "Menominee",
       :format "C%sT",
       :until -21506400000}
      {:offset -18000000,
       :rule "-",
       :format "EST",
       :until 104896800000}]},
    "Asia/Bahrain" "Asia/Qatar",
    "Atlantic/St_Helena" "Africa/Abidjan",
    "MET"
    {:current
     {:offset 3600000, :rule "C-Eur", :format "ME%sT", :from nil},
     :history []},
    "Africa/Tunis"
    {:current
     {:offset 3600000,
      :rule "Tunisia",
      :format "CE%sT",
      :from -1855958400000},
     :history
     [{:offset 2444000, :rule "-", :format "LMT", :until -2797200000000}
      {:offset 561000,
       :rule "-",
       :format "PMT",
       :until -1855958400000}]},
    "Africa/Lusaka" "Africa/Maputo",
    "CST6CDT"
    {:current {:offset -21600000, :rule "US", :format "C%sT", :from nil},
     :history []},
    "Pacific/Easter"
    {:current
     {:offset -21600000,
      :rule "Chile",
      :format "-06/-05",
      :from 384926400000},
     :history
     [{:offset -26248000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -26248000,
       :rule "-",
       :format "EMT",
       :until -1178150400000}
      {:offset -25200000,
       :rule "Chile",
       :format "-07/-06",
       :until 384926400000}]},
    "Africa/Cairo"
    {:current
     {:offset 7200000,
      :rule "Egypt",
      :format "EE%sT",
      :from -2185401600000},
     :history
     [{:offset 7509000,
       :rule "-",
       :format "LMT",
       :until -2185401600000}]},
    "Pacific/Chuuk"
    {:current
     {:offset 36000000, :rule "-", :format "+10", :from -2177452800000},
     :history
     [{:offset 36428000,
       :rule "-",
       :format "LMT",
       :until -2177452800000}]},
    "America/Montserrat" "America/Port_of_Spain",
    "Pacific/Tarawa"
    {:current
     {:offset 43200000, :rule "-", :format "+12", :from -2177452800000},
     :history
     [{:offset 41524000,
       :rule "-",
       :format "LMT",
       :until -2177452800000}]},
    "America/Resolute"
    {:current
     {:offset -21600000,
      :rule "Canada",
      :format "C%sT",
      :from 1173582000000},
     :history
     [{:offset 0, :rule "-", :format "-00", :until -704937600000}
      {:offset -21600000,
       :rule "NT_YK",
       :format "C%sT",
       :until 972784800000}
      {:offset -18000000, :rule "-", :format "EST", :until 986094000000}
      {:offset -21600000,
       :rule "Canada",
       :format "C%sT",
       :until 1162087200000}
      {:offset -18000000,
       :rule "-",
       :format "EST",
       :until 1173582000000}]},
    "Pacific/Saipan" "Pacific/Guam",
    "Asia/Muscat" "Asia/Dubai",
    "Europe/Zaporozhye"
    {:current
     {:offset 7200000, :rule "EU", :format "EE%sT", :from 788918400000},
     :history
     [{:offset 8440000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 8400000,
       :rule "-",
       :format "+0220",
       :until -1441152000000}
      {:offset 7200000, :rule "-", :format "EET", :until -1247529600000}
      {:offset 10800000, :rule "-", :format "MSK", :until -894758400000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -826416000000}
      {:offset 10800000,
       :rule "Russia",
       :format "MSK/MSD",
       :until 670381200000}
      {:offset 7200000,
       :rule "E-Eur",
       :format "EE%sT",
       :until 788918400000}]},
    "Asia/Aqtau"
    {:current
     {:offset 18000000, :rule "-", :format "+05", :from 1099209600000},
     :history
     [{:offset 12064000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 14400000, :rule "-", :format "+04", :until -1247529600000}
      {:offset 18000000, :rule "-", :format "+05", :until 370742400000}
      {:offset 21600000, :rule "-", :format "+06", :until 386467200000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 670413600000}
      {:offset 14400000,
       :rule "RussiaAsia",
       :format "+04/+05",
       :until 695808000000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 780476400000}
      {:offset 14400000,
       :rule "RussiaAsia",
       :format "+04/+05",
       :until 1099209600000}]},
    "Asia/Hebron"
    {:current
     {:offset 7200000,
      :rule "Palestine",
      :format "EE%sT",
      :from 915148800000},
     :history
     [{:offset 8423000, :rule "-", :format "LMT", :until -2185401600000}
      {:offset 7200000,
       :rule "Zion",
       :format "EET/EEST",
       :until -682646400000}
      {:offset 7200000,
       :rule "EgyptAsia",
       :format "EE%sT",
       :until -81302400000}
      {:offset 7200000,
       :rule "Zion",
       :format "I%sT",
       :until 820454400000}
      {:offset 7200000,
       :rule "Jordan",
       :format "EE%sT",
       :until 915148800000}]},
    "Asia/Kuching"
    {:current
     {:offset 28800000, :rule "-", :format "+08", :from -766972800000},
     :history
     [{:offset 26480000, :rule "-", :format "LMT", :until -1383436800000}
      {:offset 27000000,
       :rule "-",
       :format "+0730",
       :until -1167609600000}
      {:offset 28800000,
       :rule "NBorneo",
       :format "+08/+0820",
       :until -879638400000}
      {:offset 32400000,
       :rule "-",
       :format "+09",
       :until -766972800000}]},
    "Pacific/Tahiti"
    {:current
     {:offset -36000000, :rule "-", :format "-10", :from -1806710400000},
     :history
     [{:offset -35896000,
       :rule "-",
       :format "LMT",
       :until -1806710400000}]},
    "America/Argentina/Jujuy"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
     :history
     [{:offset -15672000,
       :rule "-",
       :format "LMT",
       :until -2372112000000}
      {:offset -15408000,
       :rule "-",
       :format "CMT",
       :until -1567468800000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1233446400000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until -7603200000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 636508800000}
      {:offset -14400000, :rule "-", :format "-04", :until 657072000000}
      {:offset -14400000,
       :rule 3600000,
       :format "-03",
       :until 669168000000}
      {:offset -14400000, :rule "-", :format "-04", :until 686707200000}
      {:offset -10800000,
       :rule 3600000,
       :format "-02",
       :until 694224000000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 938908800000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until 952041600000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 1224288000000}]},
    "Atlantic/Faroe"
    {:current
     {:offset 0, :rule "EU", :format "WE%sT", :from 347155200000},
     :history
     [{:offset -1624000, :rule "-", :format "LMT", :until -1955750400000}
      {:offset 0, :rule "-", :format "WET", :until 347155200000}]},
    "Asia/Vladivostok"
    {:current
     {:offset 36000000, :rule "-", :format "+10", :from 1414328400000},
     :history
     [{:offset 31651000, :rule "-", :format "LMT", :until -1487289600000}
      {:offset 32400000, :rule "-", :format "+09", :until -1247529600000}
      {:offset 36000000,
       :rule "Russia",
       :format "+10/+11",
       :until 670424400000}
      {:offset 32400000,
       :rule "Russia",
       :format "+09/+10",
       :until 695826000000}
      {:offset 36000000,
       :rule "Russia",
       :format "+10/+11",
       :until 1301230800000}
      {:offset 39600000,
       :rule "-",
       :format "+11",
       :until 1414328400000}]},
    "Europe/Oslo"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 315532800000},
     :history
     [{:offset 2580000, :rule "-", :format "LMT", :until -2366755200000}
      {:offset 3600000,
       :rule "Norway",
       :format "CE%sT",
       :until -927507600000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -781048800000}
      {:offset 3600000,
       :rule "Norway",
       :format "CE%sT",
       :until 315532800000}]},
    "Asia/Oral"
    {:current
     {:offset 18000000, :rule "-", :format "+05", :from 1099209600000},
     :history
     [{:offset 12324000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 10800000, :rule "-", :format "+03", :until -1247529600000}
      {:offset 18000000, :rule "-", :format "+05", :until 354931200000}
      {:offset 18000000,
       :rule 3600000,
       :format "+06",
       :until 370742400000}
      {:offset 21600000, :rule "-", :format "+06", :until 386467200000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 606909600000}
      {:offset 14400000,
       :rule "RussiaAsia",
       :format "+04/+05",
       :until 695808000000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 701856000000}
      {:offset 14400000,
       :rule "RussiaAsia",
       :format "+04/+05",
       :until 1099209600000}]},
    "Arctic/Longyearbyen" "Europe/Oslo",
    "America/Indiana/Marengo"
    {:current
     {:offset -18000000,
      :rule "US",
      :format "E%sT",
      :from 1136073600000},
     :history
     [{:offset -20723000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -599616000000}
      {:offset -21600000,
       :rule "Marengo",
       :format "C%sT",
       :until -273708000000}
      {:offset -18000000, :rule "-", :format "EST", :until -31536000000}
      {:offset -18000000,
       :rule "US",
       :format "E%sT",
       :until 126669600000}
      {:offset -21600000,
       :rule 3600000,
       :format "CDT",
       :until 152071200000}
      {:offset -18000000,
       :rule "US",
       :format "E%sT",
       :until 189302400000}
      {:offset -18000000,
       :rule "-",
       :format "EST",
       :until 1136073600000}]},
    "America/Rainy_River"
    {:current
     {:offset -21600000,
      :rule "Canada",
      :format "C%sT",
      :from -880250400000},
     :history
     [{:offset -22696000,
       :rule "-",
       :format "LMT",
       :until -2366755200000}
      {:offset -21600000,
       :rule "Canada",
       :format "C%sT",
       :until -923270400000}
      {:offset -21600000,
       :rule 3600000,
       :format "CDT",
       :until -880250400000}]},
    "America/Barbados"
    {:current
     {:offset -14400000,
      :rule "Barb",
      :format "A%sT",
      :from -1199232000000},
     :history
     [{:offset -14309000,
       :rule "-",
       :format "LMT",
       :until -1451692800000}
      {:offset -14309000,
       :rule "-",
       :format "BMT",
       :until -1199232000000}]},
    "Indian/Chagos"
    {:current
     {:offset 21600000, :rule "-", :format "+06", :from 820454400000},
     :history
     [{:offset 17380000, :rule "-", :format "LMT", :until -1988150400000}
      {:offset 18000000,
       :rule "-",
       :format "+05",
       :until 820454400000}]},
    "Indian/Cocos"
    {:current
     {:offset 23400000,
      :rule "-",
      :format "+0630",
      :from -2208988800000},
     :history
     [{:offset 23260000,
       :rule "-",
       :format "LMT",
       :until -2208988800000}]},
    "Africa/Harare" "Africa/Maputo",
    "America/Aruba" "America/Curacao",
    "America/El_Salvador"
    {:current
     {:offset -21600000,
      :rule "Salv",
      :format "C%sT",
      :from -1546300800000},
     :history
     [{:offset -21408000,
       :rule "-",
       :format "LMT",
       :until -1546300800000}]},
    "America/Porto_Velho"
    {:current
     {:offset -14400000, :rule "-", :format "-04", :from 590025600000},
     :history
     [{:offset -15336000,
       :rule "-",
       :format "LMT",
       :until -1767225600000}
      {:offset -14400000,
       :rule "Brazil",
       :format "-04/-03",
       :until 590025600000}]},
    "Australia/Hobart"
    {:current
     {:offset 36000000, :rule "AT", :format "AE%sT", :from -94694400000},
     :history
     [{:offset 35356000, :rule "-", :format "LMT", :until -2345760000000}
      {:offset 36000000,
       :rule "-",
       :format "AEST",
       :until -1680472800000}
      {:offset 36000000,
       :rule 3600000,
       :format "AEDT",
       :until -1669852800000}
      {:offset 36000000,
       :rule "Aus",
       :format "AE%sT",
       :until -94694400000}]},
    "America/Ojinaga"
    {:current
     {:offset -25200000,
      :rule "US",
      :format "M%sT",
      :from 1262304000000},
     :history
     [{:offset -25060000,
       :rule "-",
       :format "LMT",
       :until -1514764800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1343091600000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until -1234828800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1220317200000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until -1207180800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1191369600000}
      {:offset -21600000, :rule "-", :format "CST", :until 820454400000}
      {:offset -21600000,
       :rule "Mexico",
       :format "C%sT",
       :until 883612800000}
      {:offset -21600000, :rule "-", :format "CST", :until 891388800000}
      {:offset -25200000,
       :rule "Mexico",
       :format "M%sT",
       :until 1262304000000}]},
    "Asia/Jayapura"
    {:current
     {:offset 32400000, :rule "-", :format "WIT", :from -189388800000},
     :history
     [{:offset 33768000, :rule "-", :format "LMT", :until -1172880000000}
      {:offset 32400000, :rule "-", :format "+09", :until -799459200000}
      {:offset 34200000,
       :rule "-",
       :format "+0930",
       :until -189388800000}]},
    "America/Goose_Bay"
    {:current
     {:offset -14400000,
      :rule "Canada",
      :format "A%sT",
      :from 1320105600000},
     :history
     [{:offset -14500000,
       :rule "-",
       :format "LMT",
       :until -2713910400000}
      {:offset -12652000,
       :rule "-",
       :format "NST",
       :until -1640995200000}
      {:offset -12652000,
       :rule "Canada",
       :format "N%sT",
       :until -1609459200000}
      {:offset -12652000,
       :rule "-",
       :format "NST",
       :until -1096934400000}
      {:offset -12600000,
       :rule "-",
       :format "NST",
       :until -1073001600000}
      {:offset -12600000,
       :rule "StJohns",
       :format "N%sT",
       :until -872380800000}
      {:offset -12600000,
       :rule "Canada",
       :format "N%sT",
       :until -757382400000}
      {:offset -12600000,
       :rule "StJohns",
       :format "N%sT",
       :until -119916000000}
      {:offset -14400000,
       :rule "StJohns",
       :format "A%sT",
       :until 1320105600000}]},
    "America/Panama"
    {:current
     {:offset -18000000, :rule "-", :format "EST", :from -1946937600000},
     :history
     [{:offset -19088000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -19176000,
       :rule "-",
       :format "CMT",
       :until -1946937600000}]},
    "Africa/Dar_es_Salaam" "Africa/Nairobi",
    "Asia/Tehran"
    {:current
     {:offset 12600000,
      :rule "Iran",
      :format "+0330/+0430",
      :from 283996800000},
     :history
     [{:offset 12344000, :rule "-", :format "LMT", :until -1704153600000}
      {:offset 12344000, :rule "-", :format "TMT", :until -757382400000}
      {:offset 12600000, :rule "-", :format "+0330", :until 247190400000}
      {:offset 14400000,
       :rule "Iran",
       :format "+04/+05",
       :until 283996800000}]},
    "America/Blanc-Sablon"
    {:current {:offset -14400000, :rule "-", :format "AST", :from 0},
     :history
     [{:offset -13708000,
       :rule "-",
       :format "LMT",
       :until -2713910400000}
      {:offset -14400000, :rule "Canada", :format "A%sT", :until 0}]},
    "PST8PDT"
    {:current {:offset -28800000, :rule "US", :format "P%sT", :from nil},
     :history []},
    "America/Noronha"
    {:current
     {:offset -7200000, :rule "-", :format "-02", :from 1033430400000},
     :history
     [{:offset -7780000, :rule "-", :format "LMT", :until -1767225600000}
      {:offset -7200000,
       :rule "Brazil",
       :format "-02/-01",
       :until 653529600000}
      {:offset -7200000, :rule "-", :format "-02", :until 938649600000}
      {:offset -7200000,
       :rule "Brazil",
       :format "-02/-01",
       :until 971568000000}
      {:offset -7200000, :rule "-", :format "-02", :until 1000339200000}
      {:offset -7200000,
       :rule "Brazil",
       :format "-02/-01",
       :until 1033430400000}]},
    "America/St_Kitts" "America/Port_of_Spain",
    "Pacific/Enderbury"
    {:current
     {:offset 46800000, :rule "-", :format "+13", :from 788832000000},
     :history
     [{:offset -41060000,
       :rule "-",
       :format "LMT",
       :until -2177452800000}
      {:offset -43200000, :rule "-", :format "-12", :until 307584000000}
      {:offset -39600000,
       :rule "-",
       :format "-11",
       :until 788832000000}]},
    "Antarctica/McMurdo" "Pacific/Auckland",
    "Africa/Ceuta"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 504921600000},
     :history
     [{:offset -1276000, :rule "-", :format "LMT", :until -2177539200000}
      {:offset 0, :rule "-", :format "WET", :until -1630112400000}
      {:offset 0, :rule 3600000, :format "WEST", :until -1616806800000}
      {:offset 0, :rule "-", :format "WET", :until -1451692800000}
      {:offset 0, :rule "Spain", :format "WE%sT", :until -1293840000000}
      {:offset 0,
       :rule "SpainAfrica",
       :format "WE%sT",
       :until 448243200000}
      {:offset 3600000, :rule "-", :format "CET", :until 504921600000}]},
    "Asia/Baku"
    {:current
     {:offset 14400000,
      :rule "Azer",
      :format "+04/+05",
      :from 852076800000},
     :history
     [{:offset 11964000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 10800000, :rule "-", :format "+03", :until -405129600000}
      {:offset 14400000,
       :rule "RussiaAsia",
       :format "+04/+05",
       :until 670402800000}
      {:offset 10800000,
       :rule "RussiaAsia",
       :format "+03/+04",
       :until 717570000000}
      {:offset 14400000, :rule "-", :format "+04", :until 820454400000}
      {:offset 14400000,
       :rule "EUAsia",
       :format "+04/+05",
       :until 852076800000}]},
    "Europe/Sofia"
    {:current
     {:offset 7200000, :rule "EU", :format "EE%sT", :from 852076800000},
     :history
     [{:offset 5596000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 7016000, :rule "-", :format "IMT", :until -2369520000000}
      {:offset 7200000, :rule "-", :format "EET", :until -857250000000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -788918400000}
      {:offset 3600000, :rule "-", :format "CET", :until -781045200000}
      {:offset 7200000, :rule "-", :format "EET", :until 291769200000}
      {:offset 7200000,
       :rule "Bulg",
       :format "EE%sT",
       :until 401857200000}
      {:offset 7200000,
       :rule "C-Eur",
       :format "EE%sT",
       :until 662688000000}
      {:offset 7200000,
       :rule "E-Eur",
       :format "EE%sT",
       :until 852076800000}]},
    "CET"
    {:current
     {:offset 3600000, :rule "C-Eur", :format "CE%sT", :from nil},
     :history []},
    "America/Pangnirtung"
    {:current
     {:offset -18000000,
      :rule "Canada",
      :format "E%sT",
      :from 972784800000},
     :history
     [{:offset 0, :rule "-", :format "-00", :until -1546300800000}
      {:offset -14400000,
       :rule "NT_YK",
       :format "A%sT",
       :until 796694400000}
      {:offset -18000000,
       :rule "Canada",
       :format "E%sT",
       :until 941335200000}
      {:offset -21600000,
       :rule "Canada",
       :format "C%sT",
       :until 972784800000}]},
    "America/Monterrey"
    {:current
     {:offset -21600000,
      :rule "Mexico",
      :format "C%sT",
      :from 599616000000},
     :history
     [{:offset -24076000,
       :rule "-",
       :format "LMT",
       :until -1514851200000}
      {:offset -21600000, :rule "-", :format "CST", :until 567993600000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until 599616000000}]},
    "Asia/Pontianak"
    {:current
     {:offset 25200000, :rule "-", :format "WIB", :from 567993600000},
     :history
     [{:offset 26240000, :rule "-", :format "LMT", :until -1946160000000}
      {:offset 26240000, :rule "-", :format "PMT", :until -1172880000000}
      {:offset 27000000,
       :rule "-",
       :format "+0730",
       :until -881193600000}
      {:offset 32400000, :rule "-", :format "+09", :until -766022400000}
      {:offset 27000000,
       :rule "-",
       :format "+0730",
       :until -683856000000}
      {:offset 28800000, :rule "-", :format "+08", :until -620784000000}
      {:offset 27000000,
       :rule "-",
       :format "+0730",
       :until -189388800000}
      {:offset 28800000,
       :rule "-",
       :format "WITA",
       :until 567993600000}]},
    "America/Moncton"
    {:current
     {:offset -14400000,
      :rule "Canada",
      :format "A%sT",
      :from 1167609600000},
     :history
     [{:offset -15548000,
       :rule "-",
       :format "LMT",
       :until -2715897600000}
      {:offset -18000000,
       :rule "-",
       :format "EST",
       :until -2131660800000}
      {:offset -14400000,
       :rule "Canada",
       :format "A%sT",
       :until -1167609600000}
      {:offset -14400000,
       :rule "Moncton",
       :format "A%sT",
       :until -883612800000}
      {:offset -14400000,
       :rule "Canada",
       :format "A%sT",
       :until -757382400000}
      {:offset -14400000,
       :rule "Moncton",
       :format "A%sT",
       :until 94694400000}
      {:offset -14400000,
       :rule "Canada",
       :format "A%sT",
       :until 725846400000}
      {:offset -14400000,
       :rule "Moncton",
       :format "A%sT",
       :until 1167609600000}]},
    "America/Dominica" "America/Port_of_Spain",
    "America/Swift_Current"
    {:current
     {:offset -21600000, :rule "-", :format "CST", :from 73440000000},
     :history
     [{:offset -25880000,
       :rule "-",
       :format "LMT",
       :until -2030227200000}
      {:offset -25200000,
       :rule "Canada",
       :format "M%sT",
       :until -747273600000}
      {:offset -25200000,
       :rule "Regina",
       :format "M%sT",
       :until -631152000000}
      {:offset -25200000,
       :rule "Swift",
       :format "M%sT",
       :until 73440000000}]},
    "America/Argentina/Salta"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
     :history
     [{:offset -15700000,
       :rule "-",
       :format "LMT",
       :until -2372112000000}
      {:offset -15408000,
       :rule "-",
       :format "CMT",
       :until -1567468800000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1233446400000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until -7603200000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 667958400000}
      {:offset -14400000, :rule "-", :format "-04", :until 687916800000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 938908800000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until 952041600000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 1224288000000}]},
    "America/Sitka"
    {:current
     {:offset -32400000,
      :rule "US",
      :format "AK%sT",
      :from 438998400000},
     :history
     [{:offset 53927000, :rule "-", :format "LMT", :until -3225225600000}
      {:offset -32473000,
       :rule "-",
       :format "LMT",
       :until -2188987200000}
      {:offset -28800000, :rule "-", :format "PST", :until -883612800000}
      {:offset -28800000,
       :rule "US",
       :format "P%sT",
       :until -757382400000}
      {:offset -28800000, :rule "-", :format "PST", :until -31536000000}
      {:offset -28800000,
       :rule "US",
       :format "P%sT",
       :until 436327200000}
      {:offset -32400000,
       :rule "US",
       :format "Y%sT",
       :until 438998400000}]},
    "America/Creston"
    {:current
     {:offset -25200000, :rule "-", :format "MST", :from -1627862400000},
     :history
     [{:offset -27964000,
       :rule "-",
       :format "LMT",
       :until -2713910400000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1680480000000}
      {:offset -28800000,
       :rule "-",
       :format "PST",
       :until -1627862400000}]},
    "America/St_Vincent" "America/Port_of_Spain",
    "Europe/Andorra"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 481078800000},
     :history
     [{:offset 364000, :rule "-", :format "LMT", :until -2177452800000}
      {:offset 0, :rule "-", :format "WET", :until -733881600000}
      {:offset 3600000, :rule "-", :format "CET", :until 481078800000}]},
    "Pacific/Wallis"
    {:current
     {:offset 43200000, :rule "-", :format "+12", :from -2177452800000},
     :history
     [{:offset 44120000,
       :rule "-",
       :format "LMT",
       :until -2177452800000}]},
    "Africa/Niamey" "Africa/Lagos",
    "MST7MDT"
    {:current {:offset -25200000, :rule "US", :format "M%sT", :from nil},
     :history []},
    "Asia/Manila"
    {:current
     {:offset 28800000,
      :rule "Phil",
      :format "+08/+09",
      :from -794188800000},
     :history
     [{:offset -57360000,
       :rule "-",
       :format "LMT",
       :until -3944678400000}
      {:offset 29040000, :rule "-", :format "LMT", :until -2229292800000}
      {:offset 28800000,
       :rule "Phil",
       :format "+08/+09",
       :until -873244800000}
      {:offset 32400000,
       :rule "-",
       :format "+09",
       :until -794188800000}]},
    "HST"
    {:current {:offset -36000000, :rule "-", :format "HST", :from nil},
     :history []},
    "Asia/Shanghai"
    {:current
     {:offset 28800000,
      :rule "PRC",
      :format "C%sT",
      :from -662688000000},
     :history
     [{:offset 29143000, :rule "-", :format "LMT", :until -2177452800000}
      {:offset 28800000,
       :rule "Shang",
       :format "C%sT",
       :until -662688000000}]},
    "Europe/London"
    {:current
     {:offset 0, :rule "EU", :format "GMT/BST", :from 820454400000},
     :history
     [{:offset -75000, :rule "-", :format "LMT", :until -3852662400000}
      {:offset 0, :rule "GB-Eire", :format "%s", :until -37238400000}
      {:offset 3600000, :rule "-", :format "BST", :until 57726000000}
      {:offset 0, :rule "GB-Eire", :format "%s", :until 820454400000}]},
    "Europe/Vienna"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 347155200000},
     :history
     [{:offset 3921000, :rule "-", :format "LMT", :until -2422051200000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -1577923200000}
      {:offset 3600000,
       :rule "Austria",
       :format "CE%sT",
       :until -938894400000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -781041600000}
      {:offset 3600000,
       :rule 3600000,
       :format "CEST",
       :until -780177600000}
      {:offset 3600000, :rule "-", :format "CET", :until -757382400000}
      {:offset 3600000,
       :rule "Austria",
       :format "CE%sT",
       :until 347155200000}]},
    "Africa/Kigali" "Africa/Maputo",
    "Pacific/Auckland"
    {:current
     {:offset 43200000,
      :rule "NZ",
      :format "NZ%sT",
      :from -757382400000},
     :history
     [{:offset 41944000, :rule "-", :format "LMT", :until -3192393600000}
      {:offset 41400000,
       :rule "NZ",
       :format "NZ%sT",
       :until -757382400000}]},
    "EET"
    {:current {:offset 7200000, :rule "EU", :format "EE%sT", :from nil},
     :history []},
    "Europe/Luxembourg"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 220924800000},
     :history
     [{:offset 1476000, :rule "-", :format "LMT", :until -2069712000000}
      {:offset 3600000,
       :rule "Lux",
       :format "CE%sT",
       :until -1612656000000}
      {:offset 0, :rule "Lux", :format "WE%sT", :until -1269806400000}
      {:offset 0, :rule "Belgium", :format "WE%sT", :until -935182800000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "WE%sT",
       :until -797979600000}
      {:offset 3600000,
       :rule "Belgium",
       :format "CE%sT",
       :until 220924800000}]},
    "Europe/Bucharest"
    {:current
     {:offset 7200000, :rule "EU", :format "EE%sT", :from 852076800000},
     :history
     [{:offset 6264000, :rule "-", :format "LMT", :until -2469398400000}
      {:offset 6264000, :rule "-", :format "BMT", :until -1213142400000}
      {:offset 7200000,
       :rule "Romania",
       :format "EE%sT",
       :until 354689064000}
      {:offset 7200000,
       :rule "C-Eur",
       :format "EE%sT",
       :until 662688000000}
      {:offset 7200000,
       :rule "Romania",
       :format "EE%sT",
       :until 757382400000}
      {:offset 7200000,
       :rule "E-Eur",
       :format "EE%sT",
       :until 852076800000}]},
    "Asia/Khandyga"
    {:current
     {:offset 32400000, :rule "-", :format "+09", :from 1414332000000},
     :history
     [{:offset 32533000, :rule "-", :format "LMT", :until -1579392000000}
      {:offset 28800000, :rule "-", :format "+08", :until -1247529600000}
      {:offset 32400000,
       :rule "Russia",
       :format "+09/+10",
       :until 670420800000}
      {:offset 28800000,
       :rule "Russia",
       :format "+08/+09",
       :until 695822400000}
      {:offset 32400000,
       :rule "Russia",
       :format "+09/+10",
       :until 1072915200000}
      {:offset 36000000,
       :rule "Russia",
       :format "+10/+11",
       :until 1301230800000}
      {:offset 39600000, :rule "-", :format "+11", :until 1315915200000}
      {:offset 36000000,
       :rule "-",
       :format "+10",
       :until 1414332000000}]},
    "Asia/Kolkata"
    {:current
     {:offset 19800000, :rule "-", :format "IST", :from -764121600000},
     :history
     [{:offset 21208000, :rule "-", :format "LMT", :until -3645216000000}
      {:offset 21200000, :rule "-", :format "HMT", :until -3155673600000}
      {:offset 19270000, :rule "-", :format "MMT", :until -2019686400000}
      {:offset 19800000, :rule "-", :format "IST", :until -891561600000}
      {:offset 19800000,
       :rule 3600000,
       :format "+0630",
       :until -872035200000}
      {:offset 19800000, :rule "-", :format "IST", :until -862617600000}
      {:offset 19800000,
       :rule 3600000,
       :format "+0630",
       :until -764121600000}]},
    "Pacific/Gambier"
    {:current
     {:offset -32400000, :rule "-", :format "-09", :from -1806710400000},
     :history
     [{:offset -32388000,
       :rule "-",
       :format "LMT",
       :until -1806710400000}]},
    "Asia/Dushanbe"
    {:current
     {:offset 18000000, :rule "-", :format "+05", :from 684410400000},
     :history
     [{:offset 16512000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 18000000, :rule "-", :format "+05", :until -1247529600000}
      {:offset 21600000,
       :rule "RussiaAsia",
       :format "+06/+07",
       :until 670410000000}
      {:offset 18000000,
       :rule 3600000,
       :format "+05/+06",
       :until 684410400000}]},
    "Asia/Choibalsan"
    {:current
     {:offset 28800000,
      :rule "Mongol",
      :format "+08/+09",
      :from 1206921600000},
     :history
     [{:offset 27480000, :rule "-", :format "LMT", :until -2032905600000}
      {:offset 25200000, :rule "-", :format "+07", :until 252460800000}
      {:offset 28800000, :rule "-", :format "+08", :until 418003200000}
      {:offset 32400000,
       :rule "Mongol",
       :format "+09/+10",
       :until 1206921600000}]},
    "Asia/Phnom_Penh" "Asia/Bangkok",
    "Pacific/Guadalcanal"
    {:current
     {:offset 39600000, :rule "-", :format "+11", :from -1806710400000},
     :history
     [{:offset 38388000,
       :rule "-",
       :format "LMT",
       :until -1806710400000}]},
    "Europe/Tirane"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 457488000000},
     :history
     [{:offset 4760000, :rule "-", :format "LMT", :until -1767225600000}
      {:offset 3600000, :rule "-", :format "CET", :until -932342400000}
      {:offset 3600000,
       :rule "Albania",
       :format "CE%sT",
       :until 457488000000}]},
    "Africa/Maseru" "Africa/Johannesburg",
    "America/Cambridge_Bay"
    {:current
     {:offset -25200000,
      :rule "Canada",
      :format "M%sT",
      :from 986094000000},
     :history
     [{:offset 0, :rule "-", :format "-00", :until -1577923200000}
      {:offset -25200000,
       :rule "NT_YK",
       :format "M%sT",
       :until 941335200000}
      {:offset -21600000,
       :rule "Canada",
       :format "C%sT",
       :until 972784800000}
      {:offset -18000000, :rule "-", :format "EST", :until 973382400000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until 986094000000}]},
    "America/Denver"
    {:current
     {:offset -25200000, :rule "US", :format "M%sT", :from -94694400000},
     :history
     [{:offset -25196000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -25200000,
       :rule "US",
       :format "M%sT",
       :until -1577923200000}
      {:offset -25200000,
       :rule "Denver",
       :format "M%sT",
       :until -883612800000}
      {:offset -25200000,
       :rule "US",
       :format "M%sT",
       :until -757382400000}
      {:offset -25200000,
       :rule "Denver",
       :format "M%sT",
       :until -94694400000}]},
    "America/Thule"
    {:current
     {:offset -14400000,
      :rule "Thule",
      :format "A%sT",
      :from -1686096000000},
     :history
     [{:offset -16508000,
       :rule "-",
       :format "LMT",
       :until -1686096000000}]},
    "America/St_Barthelemy" "America/Port_of_Spain",
    "Asia/Srednekolymsk"
    {:current
     {:offset 39600000, :rule "-", :format "+11", :from 1414332000000},
     :history
     [{:offset 36892000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 36000000, :rule "-", :format "+10", :until -1247529600000}
      {:offset 39600000,
       :rule "Russia",
       :format "+11/+12",
       :until 670428000000}
      {:offset 36000000,
       :rule "Russia",
       :format "+10/+11",
       :until 695829600000}
      {:offset 39600000,
       :rule "Russia",
       :format "+11/+12",
       :until 1301234400000}
      {:offset 43200000,
       :rule "-",
       :format "+12",
       :until 1414332000000}]},
    "America/Recife"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1033430400000},
     :history
     [{:offset -8376000, :rule "-", :format "LMT", :until -1767225600000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 653529600000}
      {:offset -10800000, :rule "-", :format "-03", :until 938649600000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 971568000000}
      {:offset -10800000, :rule "-", :format "-03", :until 1000339200000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 1033430400000}]},
    "Europe/Belgrade"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 407203200000},
     :history
     [{:offset 4920000, :rule "-", :format "LMT", :until -2713910400000}
      {:offset 3600000, :rule "-", :format "CET", :until -905824800000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -788918400000}
      {:offset 3600000, :rule "-", :format "CET", :until -777927600000}
      {:offset 3600000,
       :rule 3600000,
       :format "CEST",
       :until -766612800000}
      {:offset 3600000, :rule "-", :format "CET", :until 407203200000}]},
    "Europe/Kiev"
    {:current
     {:offset 7200000, :rule "EU", :format "EE%sT", :from 788918400000},
     :history
     [{:offset 7324000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 7324000, :rule "-", :format "KMT", :until -1441152000000}
      {:offset 7200000, :rule "-", :format "EET", :until -1247529600000}
      {:offset 10800000, :rule "-", :format "MSK", :until -892512000000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -825379200000}
      {:offset 10800000,
       :rule "Russia",
       :format "MSK/MSD",
       :until 646797600000}
      {:offset 7200000,
       :rule 3600000,
       :format "EEST",
       :until 686113200000}
      {:offset 7200000,
       :rule "E-Eur",
       :format "EE%sT",
       :until 788918400000}]},
    "Europe/Zagreb" "Europe/Belgrade",
    "Africa/Douala" "Africa/Lagos",
    "America/Argentina/La_Rioja"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
     :history
     [{:offset -16044000,
       :rule "-",
       :format "LMT",
       :until -2372112000000}
      {:offset -15408000,
       :rule "-",
       :format "CMT",
       :until -1567468800000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1233446400000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until -7603200000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 667785600000}
      {:offset -14400000, :rule "-", :format "-04", :until 673574400000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 938908800000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until 952041600000}
      {:offset -10800000, :rule "-", :format "-03", :until 1086048000000}
      {:offset -14400000, :rule "-", :format "-04", :until 1087689600000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 1224288000000}]},
    "Europe/Kaliningrad"
    {:current
     {:offset 7200000, :rule "-", :format "EET", :from 1414299600000},
     :history
     [{:offset 4920000, :rule "-", :format "LMT", :until -2422051200000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -788918400000}
      {:offset 7200000,
       :rule "Poland",
       :format "CE%sT",
       :until -757382400000}
      {:offset 10800000,
       :rule "Russia",
       :format "MSK/MSD",
       :until 606895200000}
      {:offset 7200000,
       :rule "Russia",
       :format "EE%sT",
       :until 1301209200000}
      {:offset 10800000,
       :rule "-",
       :format "+03",
       :until 1414299600000}]},
    "Asia/Gaza"
    {:current
     {:offset 7200000,
      :rule "Palestine",
      :format "EE%sT",
      :from 1325376000000},
     :history
     [{:offset 8272000, :rule "-", :format "LMT", :until -2185401600000}
      {:offset 7200000,
       :rule "Zion",
       :format "EET/EEST",
       :until -682646400000}
      {:offset 7200000,
       :rule "EgyptAsia",
       :format "EE%sT",
       :until -81302400000}
      {:offset 7200000,
       :rule "Zion",
       :format "I%sT",
       :until 820454400000}
      {:offset 7200000,
       :rule "Jordan",
       :format "EE%sT",
       :until 915148800000}
      {:offset 7200000,
       :rule "Palestine",
       :format "EE%sT",
       :until 1219968000000}
      {:offset 7200000, :rule "-", :format "EET", :until 1220227200000}
      {:offset 7200000,
       :rule "Palestine",
       :format "EE%sT",
       :until 1262304000000}
      {:offset 7200000, :rule "-", :format "EET", :until 1269648060000}
      {:offset 7200000,
       :rule "Palestine",
       :format "EE%sT",
       :until 1312156800000}
      {:offset 7200000,
       :rule "-",
       :format "EET",
       :until 1325376000000}]},
    "Asia/Yerevan"
    {:current
     {:offset 14400000,
      :rule "Armenia",
      :format "+04/+05",
      :from 1293840000000},
     :history
     [{:offset 10680000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 10800000, :rule "-", :format "+03", :until -405129600000}
      {:offset 14400000,
       :rule "RussiaAsia",
       :format "+04/+05",
       :until 670402800000}
      {:offset 10800000,
       :rule "RussiaAsia",
       :format "+03/+04",
       :until 811926000000}
      {:offset 14400000, :rule "-", :format "+04", :until 852076800000}
      {:offset 14400000,
       :rule "RussiaAsia",
       :format "+04/+05",
       :until 1293840000000}]},
    "Europe/Warsaw"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 567993600000},
     :history
     [{:offset 5040000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 5040000, :rule "-", :format "WMT", :until -1717027200000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -1618693200000}
      {:offset 7200000,
       :rule "Poland",
       :format "EE%sT",
       :until -1501718400000}
      {:offset 3600000,
       :rule "Poland",
       :format "CE%sT",
       :until -931730400000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -796867200000}
      {:offset 3600000,
       :rule "Poland",
       :format "CE%sT",
       :until 220924800000}
      {:offset 3600000,
       :rule "W-Eur",
       :format "CE%sT",
       :until 567993600000}]},
    "Australia/Currie"
    {:current
     {:offset 36000000, :rule "AT", :format "AE%sT", :from 47174400000},
     :history
     [{:offset 34528000, :rule "-", :format "LMT", :until -2345760000000}
      {:offset 36000000,
       :rule "-",
       :format "AEST",
       :until -1680472800000}
      {:offset 36000000,
       :rule 3600000,
       :format "AEDT",
       :until -1669852800000}
      {:offset 36000000,
       :rule "Aus",
       :format "AE%sT",
       :until 47174400000}]},
    "Africa/Nouakchott" "Africa/Abidjan",
    "Africa/Brazzaville" "Africa/Lagos",
    "Asia/Yekaterinburg"
    {:current
     {:offset 18000000, :rule "-", :format "+05", :from 1414310400000},
     :history
     [{:offset 14553000, :rule "-", :format "LMT", :until -1688256000000}
      {:offset 13505000, :rule "-", :format "PMT", :until -1592596800000}
      {:offset 14400000, :rule "-", :format "+04", :until -1247529600000}
      {:offset 18000000,
       :rule "Russia",
       :format "+05/+06",
       :until 670406400000}
      {:offset 14400000,
       :rule "Russia",
       :format "+04/+05",
       :until 695808000000}
      {:offset 18000000,
       :rule "Russia",
       :format "+05/+06",
       :until 1301212800000}
      {:offset 21600000,
       :rule "-",
       :format "+06",
       :until 1414310400000}]},
    "America/Belize"
    {:current
     {:offset -21600000,
      :rule "Belize",
      :format "%s",
      :from -1822521600000},
     :history
     [{:offset -21168000,
       :rule "-",
       :format "LMT",
       :until -1822521600000}]},
    "Atlantic/Azores"
    {:current
     {:offset -3600000,
      :rule "EU",
      :format "-01/+00",
      :from 733287600000},
     :history
     [{:offset -6160000, :rule "-", :format "LMT", :until -2713910400000}
      {:offset -6872000, :rule "-", :format "HMT", :until -1830373200000}
      {:offset -7200000,
       :rule "Port",
       :format "-02/-01",
       :until -873683672000}
      {:offset -7200000,
       :rule "Port",
       :format "+00",
       :until -864007200000}
      {:offset -7200000,
       :rule "Port",
       :format "-02/-01",
       :until -842839200000}
      {:offset -7200000,
       :rule "Port",
       :format "+00",
       :until -831348000000}
      {:offset -7200000,
       :rule "Port",
       :format "-02/-01",
       :until -810784800000}
      {:offset -7200000,
       :rule "Port",
       :format "+00",
       :until -799898400000}
      {:offset -7200000,
       :rule "Port",
       :format "-02/-01",
       :until -779338800000}
      {:offset -7200000,
       :rule "Port",
       :format "+00",
       :until -768448800000}
      {:offset -7200000,
       :rule "Port",
       :format "-02/-01",
       :until -118274400000}
      {:offset -3600000,
       :rule "Port",
       :format "-01/+00",
       :until 433299600000}
      {:offset -3600000,
       :rule "W-Eur",
       :format "-01/+00",
       :until 717559200000}
      {:offset 0, :rule "EU", :format "WE%sT", :until 733287600000}]},
    "America/Argentina/San_Juan"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
     :history
     [{:offset -16444000,
       :rule "-",
       :format "LMT",
       :until -2372112000000}
      {:offset -15408000,
       :rule "-",
       :format "CMT",
       :until -1567468800000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1233446400000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until -7603200000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 667785600000}
      {:offset -14400000, :rule "-", :format "-04", :until 673574400000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 938908800000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until 952041600000}
      {:offset -10800000, :rule "-", :format "-03", :until 1085961600000}
      {:offset -14400000, :rule "-", :format "-04", :until 1090713600000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 1224288000000}]},
    "Asia/Kamchatka"
    {:current
     {:offset 43200000, :rule "-", :format "+12", :from 1301241600000},
     :history
     [{:offset 38076000, :rule "-", :format "LMT", :until -1487721600000}
      {:offset 39600000, :rule "-", :format "+11", :until -1247529600000}
      {:offset 43200000,
       :rule "Russia",
       :format "+12/+13",
       :until 670431600000}
      {:offset 39600000,
       :rule "Russia",
       :format "+11/+12",
       :until 695833200000}
      {:offset 43200000,
       :rule "Russia",
       :format "+12/+13",
       :until 1269788400000}
      {:offset 39600000,
       :rule "Russia",
       :format "+11/+12",
       :until 1301241600000}]},
    "Australia/Adelaide"
    {:current
     {:offset 34200000, :rule "AS", :format "AC%sT", :from 31536000000},
     :history
     [{:offset 33260000, :rule "-", :format "LMT", :until -2364076800000}
      {:offset 32400000,
       :rule "-",
       :format "ACST",
       :until -2230156800000}
      {:offset 34200000,
       :rule "Aus",
       :format "AC%sT",
       :until 31536000000}]},
    "MST"
    {:current {:offset -25200000, :rule "-", :format "MST", :from nil},
     :history []},
    "America/Rankin_Inlet"
    {:current
     {:offset -21600000,
      :rule "Canada",
      :format "C%sT",
      :from 986094000000},
     :history
     [{:offset 0, :rule "-", :format "-00", :until -410227200000}
      {:offset -21600000,
       :rule "NT_YK",
       :format "C%sT",
       :until 972784800000}
      {:offset -18000000,
       :rule "-",
       :format "EST",
       :until 986094000000}]},
    "Pacific/Kwajalein"
    {:current
     {:offset 43200000, :rule "-", :format "+12", :from 745804800000},
     :history
     [{:offset 40160000, :rule "-", :format "LMT", :until -2177452800000}
      {:offset 39600000, :rule "-", :format "+11", :until -7948800000}
      {:offset -43200000,
       :rule "-",
       :format "-12",
       :until 745804800000}]},
    "Asia/Yangon"
    {:current
     {:offset 23400000, :rule "-", :format "+0630", :from -778377600000},
     :history
     [{:offset 23087000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 23087000, :rule "-", :format "RMT", :until -1577923200000}
      {:offset 23400000,
       :rule "-",
       :format "+0630",
       :until -873244800000}
      {:offset 32400000,
       :rule "-",
       :format "+09",
       :until -778377600000}]},
    "America/Tortola" "America/Port_of_Spain",
    "Europe/Volgograd"
    {:current
     {:offset 10800000, :rule "-", :format "+03", :from 1414303200000},
     :history
     [{:offset 10660000, :rule "-", :format "LMT", :until -1577750400000}
      {:offset 10800000, :rule "-", :format "+03", :until -1247529600000}
      {:offset 14400000, :rule "-", :format "+04", :until -256867200000}
      {:offset 14400000,
       :rule "Russia",
       :format "+04/+05",
       :until 575452800000}
      {:offset 10800000,
       :rule "Russia",
       :format "+03/+04",
       :until 670406400000}
      {:offset 14400000, :rule "-", :format "+04", :until 701852400000}
      {:offset 10800000,
       :rule "Russia",
       :format "+03/+04",
       :until 1301212800000}
      {:offset 14400000,
       :rule "-",
       :format "+04",
       :until 1414303200000}]},
    "America/Inuvik"
    {:current
     {:offset -25200000,
      :rule "Canada",
      :format "M%sT",
      :from 315532800000},
     :history
     [{:offset 0, :rule "-", :format "-00", :until -536457600000}
      {:offset -28800000,
       :rule "NT_YK",
       :format "P%sT",
       :until 294192000000}
      {:offset -25200000,
       :rule "NT_YK",
       :format "M%sT",
       :until 315532800000}]},
    "Asia/Pyongyang"
    {:current
     {:offset 32400000, :rule "-", :format "KST", :from 1525478400000},
     :history
     [{:offset 30180000, :rule "-", :format "LMT", :until -1948752000000}
      {:offset 30600000, :rule "-", :format "KST", :until -1830384000000}
      {:offset 32400000, :rule "-", :format "JST", :until -768614400000}
      {:offset 32400000, :rule "-", :format "KST", :until 1439596800000}
      {:offset 30600000,
       :rule "-",
       :format "KST",
       :until 1525478400000}]},
    "Asia/Vientiane" "Asia/Bangkok",
    "Africa/Lubumbashi" "Africa/Maputo",
    "America/Dawson"
    {:current
     {:offset -28800000,
      :rule "Canada",
      :format "P%sT",
      :from 315532800000},
     :history
     [{:offset -33460000,
       :rule "-",
       :format "LMT",
       :until -2189030400000}
      {:offset -32400000,
       :rule "NT_YK",
       :format "Y%sT",
       :until 120614400000}
      {:offset -28800000,
       :rule "NT_YK",
       :format "P%sT",
       :until 315532800000}]},
    "Asia/Yakutsk"
    {:current
     {:offset 32400000, :rule "-", :format "+09", :from 1414324800000},
     :history
     [{:offset 31138000, :rule "-", :format "LMT", :until -1579392000000}
      {:offset 28800000, :rule "-", :format "+08", :until -1247529600000}
      {:offset 32400000,
       :rule "Russia",
       :format "+09/+10",
       :until 670420800000}
      {:offset 28800000,
       :rule "Russia",
       :format "+08/+09",
       :until 695822400000}
      {:offset 32400000,
       :rule "Russia",
       :format "+09/+10",
       :until 1301227200000}
      {:offset 36000000,
       :rule "-",
       :format "+10",
       :until 1414324800000}]},
    "Europe/San_Marino" "Europe/Rome",
    "Pacific/Rarotonga"
    {:current
     {:offset -36000000,
      :rule "Cook",
      :format "-10/-0930",
      :from 279676800000},
     :history
     [{:offset -38344000,
       :rule "-",
       :format "LMT",
       :until -2177452800000}
      {:offset -37800000,
       :rule "-",
       :format "-1030",
       :until 279676800000}]},
    "America/Curacao"
    {:current
     {:offset -14400000, :rule "-", :format "AST", :from -157766400000},
     :history
     [{:offset -16547000,
       :rule "-",
       :format "LMT",
       :until -1826755200000}
      {:offset -16200000,
       :rule "-",
       :format "-0430",
       :until -157766400000}]},
    "America/La_Paz"
    {:current
     {:offset -14400000, :rule "-", :format "-04", :from -1192320000000},
     :history
     [{:offset -16356000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -16356000,
       :rule "-",
       :format "CMT",
       :until -1205971200000}
      {:offset -16356000,
       :rule 3600000,
       :format "BST",
       :until -1192320000000}]},
    "Europe/Astrakhan"
    {:current
     {:offset 14400000, :rule "-", :format "+04", :from 1459065600000},
     :history
     [{:offset 11532000, :rule "-", :format "LMT", :until -1441238400000}
      {:offset 10800000, :rule "-", :format "+03", :until -1247529600000}
      {:offset 14400000,
       :rule "Russia",
       :format "+04/+05",
       :until 606898800000}
      {:offset 10800000,
       :rule "Russia",
       :format "+03/+04",
       :until 670406400000}
      {:offset 14400000, :rule "-", :format "+04", :until 701852400000}
      {:offset 10800000,
       :rule "Russia",
       :format "+03/+04",
       :until 1301212800000}
      {:offset 14400000, :rule "-", :format "+04", :until 1414303200000}
      {:offset 10800000,
       :rule "-",
       :format "+03",
       :until 1459065600000}]},
    "Europe/Prague"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 283996800000},
     :history
     [{:offset 3464000, :rule "-", :format "LMT", :until -3786825600000}
      {:offset 3464000, :rule "-", :format "PMT", :until -2469398400000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -777859200000}
      {:offset 3600000,
       :rule "Czech",
       :format "CE%sT",
       :until -728514000000}
      {:offset 3600000,
       :rule -3600000,
       :format "GMT",
       :until -721260000000}
      {:offset 3600000,
       :rule "Czech",
       :format "CE%sT",
       :until 283996800000}]},
    "America/Chihuahua"
    {:current
     {:offset -25200000,
      :rule "Mexico",
      :format "M%sT",
      :from 891388800000},
     :history
     [{:offset -25460000,
       :rule "-",
       :format "LMT",
       :until -1514851200000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1343091600000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until -1234828800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1220317200000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until -1207180800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1191369600000}
      {:offset -21600000, :rule "-", :format "CST", :until 820454400000}
      {:offset -21600000,
       :rule "Mexico",
       :format "C%sT",
       :until 883612800000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until 891388800000}]},
    "America/Los_Angeles"
    {:current
     {:offset -28800000, :rule "US", :format "P%sT", :from -94694400000},
     :history
     [{:offset -28378000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -28800000,
       :rule "US",
       :format "P%sT",
       :until -757382400000}
      {:offset -28800000,
       :rule "CA",
       :format "P%sT",
       :until -94694400000}]},
    "America/Lima"
    {:current
     {:offset -18000000,
      :rule "Peru",
      :format "-05/-04",
      :from -1938556800000},
     :history
     [{:offset -18492000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -18516000,
       :rule "-",
       :format "LMT",
       :until -1938556800000}]},
    "America/Caracas"
    {:current
     {:offset -14400000, :rule "-", :format "-04", :from 1462069800000},
     :history
     [{:offset -16064000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -16060000,
       :rule "-",
       :format "CMT",
       :until -1826755200000}
      {:offset -16200000,
       :rule "-",
       :format "-0430",
       :until -157766400000}
      {:offset -14400000, :rule "-", :format "-04", :until 1197169200000}
      {:offset -16200000,
       :rule "-",
       :format "-0430",
       :until 1462069800000}]},
    "Africa/Dakar" "Africa/Abidjan",
    "Asia/Nicosia"
    {:current
     {:offset 7200000,
      :rule "EUAsia",
      :format "EE%sT",
      :from 904608000000},
     :history
     [{:offset 8008000, :rule "-", :format "LMT", :until -1518912000000}
      {:offset 7200000,
       :rule "Cyprus",
       :format "EE%sT",
       :until 904608000000}]},
    "America/Indiana/Petersburg"
    {:current
     {:offset -18000000,
      :rule "US",
      :format "E%sT",
      :from 1194141600000},
     :history
     [{:offset -20947000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -473385600000}
      {:offset -21600000,
       :rule "Pike",
       :format "C%sT",
       :until -147909600000}
      {:offset -18000000, :rule "-", :format "EST", :until -100130400000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until 247024800000}
      {:offset -18000000, :rule "-", :format "EST", :until 1143943200000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until 1194141600000}]},
    "America/Tijuana"
    {:current
     {:offset -28800000,
      :rule "US",
      :format "P%sT",
      :from 1262304000000},
     :history
     [{:offset -28084000,
       :rule "-",
       :format "LMT",
       :until -1514764800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1451692800000}
      {:offset -28800000,
       :rule "-",
       :format "PST",
       :until -1343091600000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1234828800000}
      {:offset -28800000,
       :rule "-",
       :format "PST",
       :until -1222992000000}
      {:offset -28800000,
       :rule 3600000,
       :format "PDT",
       :until -1207267200000}
      {:offset -28800000, :rule "-", :format "PST", :until -873849600000}
      {:offset -28800000,
       :rule 3600000,
       :format "PWT",
       :until -769388400000}
      {:offset -28800000,
       :rule 3600000,
       :format "PPT",
       :until -761702400000}
      {:offset -28800000, :rule "-", :format "PST", :until -686102400000}
      {:offset -28800000,
       :rule 3600000,
       :format "PDT",
       :until -661564800000}
      {:offset -28800000, :rule "-", :format "PST", :until -504921600000}
      {:offset -28800000,
       :rule "CA",
       :format "P%sT",
       :until -283996800000}
      {:offset -28800000, :rule "-", :format "PST", :until 189302400000}
      {:offset -28800000,
       :rule "US",
       :format "P%sT",
       :until 820454400000}
      {:offset -28800000,
       :rule "Mexico",
       :format "P%sT",
       :until 978307200000}
      {:offset -28800000,
       :rule "US",
       :format "P%sT",
       :until 1014163200000}
      {:offset -28800000,
       :rule "Mexico",
       :format "P%sT",
       :until 1262304000000}]},
    "Europe/Guernsey" "Europe/London",
    "Africa/Conakry" "Africa/Abidjan",
    "Europe/Dublin"
    {:current
     {:offset 3600000,
      :rule "Eire",
      :format "IST/GMT",
      :from -37238400000},
     :history
     [{:offset -1500000, :rule "-", :format "LMT", :until -2821651200000}
      {:offset -1521000, :rule "-", :format "DMT", :until -1691961900000}
      {:offset -1521000,
       :rule 3600000,
       :format "IST",
       :until -1680470721000}
      {:offset 0, :rule "GB-Eire", :format "%s", :until -1517011200000}
      {:offset 0,
       :rule "GB-Eire",
       :format "GMT/IST",
       :until -942008400000}
      {:offset 0, :rule 3600000, :format "IST", :until -733352400000}
      {:offset 0, :rule "-", :format "GMT", :until -719442000000}
      {:offset 0, :rule 3600000, :format "IST", :until -699483600000}
      {:offset 0, :rule "-", :format "GMT", :until -684968400000}
      {:offset 0,
       :rule "GB-Eire",
       :format "GMT/IST",
       :until -37238400000}]},
    "Europe/Helsinki"
    {:current
     {:offset 7200000, :rule "EU", :format "EE%sT", :from 410227200000},
     :history
     [{:offset 5989000, :rule "-", :format "LMT", :until -2890252800000}
      {:offset 5989000, :rule "-", :format "HMT", :until -1535932800000}
      {:offset 7200000,
       :rule "Finland",
       :format "EE%sT",
       :until 410227200000}]},
    "Africa/Abidjan"
    {:current
     {:offset 0, :rule "-", :format "GMT", :from -1830384000000},
     :history
     [{:offset -968000,
       :rule "-",
       :format "LMT",
       :until -1830384000000}]},
    "America/Campo_Grande"
    {:current
     {:offset -14400000,
      :rule "Brazil",
      :format "-04/-03",
      :from -1767225600000},
     :history
     [{:offset -13108000,
       :rule "-",
       :format "LMT",
       :until -1767225600000}]},
    "America/Guatemala"
    {:current
     {:offset -21600000,
      :rule "Guat",
      :format "C%sT",
      :from -1617062400000},
     :history
     [{:offset -21724000,
       :rule "-",
       :format "LMT",
       :until -1617062400000}]},
    "Pacific/Tongatapu"
    {:current
     {:offset 46800000,
      :rule "Tonga",
      :format "+13/+14",
      :from 915148800000},
     :history
     [{:offset 44360000, :rule "-", :format "LMT", :until -2177452800000}
      {:offset 44400000,
       :rule "-",
       :format "+1220",
       :until -915148800000}
      {:offset 46800000,
       :rule "-",
       :format "+13",
       :until 915148800000}]},
    "Africa/Monrovia"
    {:current {:offset 0, :rule "-", :format "GMT", :from 63590400000},
     :history
     [{:offset -2588000, :rule "-", :format "LMT", :until -2776982400000}
      {:offset -2588000, :rule "-", :format "MMT", :until -1604361600000}
      {:offset -2670000, :rule "-", :format "MMT", :until 63590400000}]},
    "Atlantic/Bermuda"
    {:current
     {:offset -14400000, :rule "US", :format "A%sT", :from 189302400000},
     :history
     [{:offset -15558000,
       :rule "-",
       :format "LMT",
       :until -1262304000000}
      {:offset -14400000, :rule "-", :format "AST", :until 136346400000}
      {:offset -14400000,
       :rule "Canada",
       :format "A%sT",
       :until 189302400000}]},
    "Asia/Almaty"
    {:current
     {:offset 21600000, :rule "-", :format "+06", :from 1099209600000},
     :history
     [{:offset 18468000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 18000000, :rule "-", :format "+05", :until -1247529600000}
      {:offset 21600000,
       :rule "RussiaAsia",
       :format "+06/+07",
       :until 670410000000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 695811600000}
      {:offset 21600000,
       :rule "RussiaAsia",
       :format "+06/+07",
       :until 1099209600000}]},
    "Asia/Bishkek"
    {:current
     {:offset 21600000, :rule "-", :format "+06", :from 1123804800000},
     :history
     [{:offset 17904000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 18000000, :rule "-", :format "+05", :until -1247529600000}
      {:offset 21600000,
       :rule "RussiaAsia",
       :format "+06/+07",
       :until 670410000000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 683604000000}
      {:offset 18000000,
       :rule "Kyrgyz",
       :format "+05/+06",
       :until 1123804800000}]},
    "Indian/Mayotte" "Africa/Nairobi",
    "America/Martinique"
    {:current
     {:offset -14400000, :rule "-", :format "AST", :from 338947200000},
     :history
     [{:offset -14660000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -14660000,
       :rule "-",
       :format "FFMT",
       :until -1851552000000}
      {:offset -14400000, :rule "-", :format "AST", :until 323827200000}
      {:offset -14400000,
       :rule 3600000,
       :format "ADT",
       :until 338947200000}]},
    "America/Danmarkshavn"
    {:current {:offset 0, :rule "-", :format "GMT", :from 820454400000},
     :history
     [{:offset -4480000, :rule "-", :format "LMT", :until -1686096000000}
      {:offset -10800000, :rule "-", :format "-03", :until 323834400000}
      {:offset -10800000,
       :rule "EU",
       :format "-03/-02",
       :until 820454400000}]},
    "Europe/Athens"
    {:current
     {:offset 7200000, :rule "EU", :format "EE%sT", :from 347155200000},
     :history
     [{:offset 5692000, :rule "-", :format "LMT", :until -2344636800000}
      {:offset 5692000, :rule "-", :format "AMT", :until -1686095940000}
      {:offset 7200000,
       :rule "Greece",
       :format "EE%sT",
       :until -904867200000}
      {:offset 3600000,
       :rule "Greece",
       :format "CE%sT",
       :until -812419200000}
      {:offset 7200000,
       :rule "Greece",
       :format "EE%sT",
       :until 347155200000}]},
    "Europe/Sarajevo" "Europe/Belgrade",
    "EST"
    {:current {:offset -18000000, :rule "-", :format "EST", :from nil},
     :history []},
    "America/Yellowknife"
    {:current
     {:offset -25200000,
      :rule "Canada",
      :format "M%sT",
      :from 315532800000},
     :history
     [{:offset 0, :rule "-", :format "-00", :until -1104537600000}
      {:offset -25200000,
       :rule "NT_YK",
       :format "M%sT",
       :until 315532800000}]},
    "Africa/Freetown" "Africa/Abidjan",
    "Asia/Tbilisi"
    {:current
     {:offset 14400000, :rule "-", :format "+04", :from 1111881600000},
     :history
     [{:offset 10751000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 10751000,
       :rule "-",
       :format "TBMT",
       :until -1441152000000}
      {:offset 10800000, :rule "-", :format "+03", :until -405129600000}
      {:offset 14400000,
       :rule "RussiaAsia",
       :format "+04/+05",
       :until 670402800000}
      {:offset 10800000,
       :rule "RussiaAsia",
       :format "+03/+04",
       :until 694224000000}
      {:offset 10800000,
       :rule "E-EurAsia",
       :format "+03/+04",
       :until 780451200000}
      {:offset 14400000,
       :rule "E-EurAsia",
       :format "+04/+05",
       :until 846374400000}
      {:offset 14400000,
       :rule 3600000,
       :format "+05",
       :until 859680000000}
      {:offset 14400000,
       :rule "E-EurAsia",
       :format "+04/+05",
       :until 1088294400000}
      {:offset 10800000,
       :rule "RussiaAsia",
       :format "+03/+04",
       :until 1111881600000}]},
    "Pacific/Pago_Pago"
    {:current
     {:offset -39600000, :rule "-", :format "SST", :from -1861920000000},
     :history
     [{:offset 45432000, :rule "-", :format "LMT", :until -2445379200000}
      {:offset -40968000,
       :rule "-",
       :format "LMT",
       :until -1861920000000}]},
    "America/Argentina/Ushuaia"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
     :history
     [{:offset -16392000,
       :rule "-",
       :format "LMT",
       :until -2372112000000}
      {:offset -15408000,
       :rule "-",
       :format "CMT",
       :until -1567468800000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1233446400000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until -7603200000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 938908800000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until 952041600000}
      {:offset -10800000, :rule "-", :format "-03", :until 1085875200000}
      {:offset -14400000, :rule "-", :format "-04", :until 1087689600000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 1224288000000}]},
    "America/Metlakatla"
    {:current
     {:offset -32400000,
      :rule "US",
      :format "AK%sT",
      :from 1446343200000},
     :history
     [{:offset 54822000, :rule "-", :format "LMT", :until -3225225600000}
      {:offset -31578000,
       :rule "-",
       :format "LMT",
       :until -2188987200000}
      {:offset -28800000, :rule "-", :format "PST", :until -883612800000}
      {:offset -28800000,
       :rule "US",
       :format "P%sT",
       :until -757382400000}
      {:offset -28800000, :rule "-", :format "PST", :until -31536000000}
      {:offset -28800000,
       :rule "US",
       :format "P%sT",
       :until 436327200000}
      {:offset -28800000,
       :rule "-",
       :format "PST",
       :until 1446343200000}]},
    "America/Boa_Vista"
    {:current
     {:offset -14400000, :rule "-", :format "-04", :from 971568000000},
     :history
     [{:offset -14560000,
       :rule "-",
       :format "LMT",
       :until -1767225600000}
      {:offset -14400000,
       :rule "Brazil",
       :format "-04/-03",
       :until 590025600000}
      {:offset -14400000, :rule "-", :format "-04", :until 938649600000}
      {:offset -14400000,
       :rule "Brazil",
       :format "-04/-03",
       :until 971568000000}]},
    "Pacific/Nauru"
    {:current
     {:offset 43200000, :rule "-", :format "+12", :from 294364800000},
     :history
     [{:offset 40060000, :rule "-", :format "LMT", :until -1545091200000}
      {:offset 41400000,
       :rule "-",
       :format "+1130",
       :until -877305600000}
      {:offset 32400000, :rule "-", :format "+09", :until -800928000000}
      {:offset 41400000,
       :rule "-",
       :format "+1130",
       :until 294364800000}]},
    "Asia/Brunei"
    {:current
     {:offset 28800000, :rule "-", :format "+08", :from -1167609600000},
     :history
     [{:offset 27580000, :rule "-", :format "LMT", :until -1383436800000}
      {:offset 27000000,
       :rule "-",
       :format "+0730",
       :until -1167609600000}]},
    "Pacific/Palau"
    {:current
     {:offset 32400000, :rule "-", :format "+09", :from -2177452800000},
     :history
     [{:offset 32276000,
       :rule "-",
       :format "LMT",
       :until -2177452800000}]},
    "Asia/Dili"
    {:current
     {:offset 32400000, :rule "-", :format "+09", :from 969148800000},
     :history
     [{:offset 30140000, :rule "-", :format "LMT", :until -1830384000000}
      {:offset 28800000, :rule "-", :format "+08", :until -879123600000}
      {:offset 32400000, :rule "-", :format "+09", :until 199929600000}
      {:offset 28800000,
       :rule "-",
       :format "+08",
       :until 969148800000}]},
    "America/Port-au-Prince"
    {:current
     {:offset -18000000,
      :rule "Haiti",
      :format "E%sT",
      :from -1670500800000},
     :history
     [{:offset -17360000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -17340000,
       :rule "-",
       :format "PPMT",
       :until -1670500800000}]},
    "Pacific/Chatham"
    {:current
     {:offset 45900000,
      :rule "Chatham",
      :format "+1245/+1345",
      :from -757382400000},
     :history
     [{:offset 44028000, :rule "-", :format "LMT", :until -3192393600000}
      {:offset 44100000,
       :rule "-",
       :format "+1215",
       :until -757382400000}]},
    "America/Indiana/Indianapolis"
    {:current
     {:offset -18000000,
      :rule "US",
      :format "E%sT",
      :from 1136073600000},
     :history
     [{:offset -20678000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -1577923200000}
      {:offset -21600000,
       :rule "Indianapolis",
       :format "C%sT",
       :until -883612800000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -757382400000}
      {:offset -21600000,
       :rule "Indianapolis",
       :format "C%sT",
       :until -463615200000}
      {:offset -18000000, :rule "-", :format "EST", :until -386805600000}
      {:offset -21600000, :rule "-", :format "CST", :until -368661600000}
      {:offset -18000000, :rule "-", :format "EST", :until -31536000000}
      {:offset -18000000, :rule "US", :format "E%sT", :until 31536000000}
      {:offset -18000000,
       :rule "-",
       :format "EST",
       :until 1136073600000}]},
    "Atlantic/Reykjavik"
    {:current {:offset 0, :rule "-", :format "GMT", :from -54775680000},
     :history
     [{:offset -5280000, :rule "-", :format "LMT", :until -1956614400000}
      {:offset -3600000,
       :rule "Iceland",
       :format "-01/+00",
       :until -54775680000}]},
    "America/Kentucky/Louisville"
    {:current
     {:offset -18000000, :rule "US", :format "E%sT", :from 152071200000},
     :history
     [{:offset -20582000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -1546300800000}
      {:offset -21600000,
       :rule "Louisville",
       :format "C%sT",
       :until -883612800000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -757382400000}
      {:offset -21600000,
       :rule "Louisville",
       :format "C%sT",
       :until -266450400000}
      {:offset -18000000, :rule "-", :format "EST", :until -63158400000}
      {:offset -18000000,
       :rule "US",
       :format "E%sT",
       :until 126669600000}
      {:offset -21600000,
       :rule 3600000,
       :format "CDT",
       :until 152071200000}]},
    "Australia/Sydney"
    {:current
     {:offset 36000000, :rule "AN", :format "AE%sT", :from 31536000000},
     :history
     [{:offset 36292000, :rule "-", :format "LMT", :until -2364076800000}
      {:offset 36000000,
       :rule "Aus",
       :format "AE%sT",
       :until 31536000000}]},
    "America/Indiana/Vincennes"
    {:current
     {:offset -18000000,
      :rule "US",
      :format "E%sT",
      :from 1194141600000},
     :history
     [{:offset -21007000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -757382400000}
      {:offset -21600000,
       :rule "Vincennes",
       :format "C%sT",
       :until -179359200000}
      {:offset -18000000, :rule "-", :format "EST", :until -31536000000}
      {:offset -18000000, :rule "US", :format "E%sT", :until 31536000000}
      {:offset -18000000, :rule "-", :format "EST", :until 1143943200000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until 1194141600000}]},
    "America/Bogota"
    {:current
     {:offset -18000000,
      :rule "CO",
      :format "-05/-04",
      :from -1739059200000},
     :history
     [{:offset -17776000,
       :rule "-",
       :format "LMT",
       :until -2707689600000}
      {:offset -17776000,
       :rule "-",
       :format "BMT",
       :until -1739059200000}]},
    "America/Phoenix"
    {:current
     {:offset -25200000, :rule "-", :format "MST", :from -56246400000},
     :history
     [{:offset -26898000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -25200000,
       :rule "US",
       :format "M%sT",
       :until -820540740000}
      {:offset -25200000, :rule "-", :format "MST", :until -812678340000}
      {:offset -25200000,
       :rule "US",
       :format "M%sT",
       :until -796867140000}
      {:offset -25200000, :rule "-", :format "MST", :until -94694400000}
      {:offset -25200000,
       :rule "US",
       :format "M%sT",
       :until -56246400000}]},
    "Africa/Djibouti" "Africa/Nairobi",
    "Africa/Banjul" "Africa/Abidjan",
    "Asia/Atyrau"
    {:current
     {:offset 18000000, :rule "-", :format "+05", :from 1099209600000},
     :history
     [{:offset 12464000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 10800000, :rule "-", :format "+03", :until -1247529600000}
      {:offset 18000000, :rule "-", :format "+05", :until 370742400000}
      {:offset 21600000, :rule "-", :format "+06", :until 386467200000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 670413600000}
      {:offset 14400000,
       :rule "RussiaAsia",
       :format "+04/+05",
       :until 695808000000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 922608000000}
      {:offset 14400000,
       :rule "RussiaAsia",
       :format "+04/+05",
       :until 1099209600000}]},
    "America/Cayenne"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from -71107200000},
     :history
     [{:offset -12560000,
       :rule "-",
       :format "LMT",
       :until -1846281600000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -71107200000}]},
    "America/Grand_Turk"
    {:current
     {:offset -18000000,
      :rule "US",
      :format "E%sT",
      :from 1520737200000},
     :history
     [{:offset -17072000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -18430000,
       :rule "-",
       :format "KMT",
       :until -1827705600000}
      {:offset -18000000, :rule "-", :format "EST", :until 283996800000}
      {:offset -18000000,
       :rule "US",
       :format "E%sT",
       :until 1446336000000}
      {:offset -14400000,
       :rule "-",
       :format "AST",
       :until 1520737200000}]},
    "Europe/Vilnius"
    {:current
     {:offset 7200000, :rule "EU", :format "EE%sT", :from 1041379200000},
     :history
     [{:offset 6076000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 5040000, :rule "-", :format "WMT", :until -1672531200000}
      {:offset 5736000, :rule "-", :format "KMT", :until -1585094400000}
      {:offset 3600000, :rule "-", :format "CET", :until -1561248000000}
      {:offset 7200000, :rule "-", :format "EET", :until -1553558400000}
      {:offset 3600000, :rule "-", :format "CET", :until -928195200000}
      {:offset 10800000, :rule "-", :format "MSK", :until -900115200000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -802137600000}
      {:offset 10800000,
       :rule "Russia",
       :format "MSK/MSD",
       :until 606891600000}
      {:offset 7200000,
       :rule "Russia",
       :format "EE%sT",
       :until 686124000000}
      {:offset 7200000,
       :rule "C-Eur",
       :format "EE%sT",
       :until 883612800000}
      {:offset 7200000, :rule "-", :format "EET", :until 891140400000}
      {:offset 3600000, :rule "EU", :format "CE%sT", :until 941335200000}
      {:offset 7200000,
       :rule "-",
       :format "EET",
       :until 1041379200000}]},
    "America/Hermosillo"
    {:current
     {:offset -25200000, :rule "-", :format "MST", :from 915148800000},
     :history
     [{:offset -26632000,
       :rule "-",
       :format "LMT",
       :until -1514851200000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1343091600000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until -1234828800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1220317200000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until -1207180800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1191369600000}
      {:offset -21600000, :rule "-", :format "CST", :until -873849600000}
      {:offset -25200000, :rule "-", :format "MST", :until -661564800000}
      {:offset -28800000, :rule "-", :format "PST", :until 0}
      {:offset -25200000,
       :rule "Mexico",
       :format "M%sT",
       :until 915148800000}]},
    "America/Yakutat"
    {:current
     {:offset -32400000,
      :rule "US",
      :format "AK%sT",
      :from 438998400000},
     :history
     [{:offset 52865000, :rule "-", :format "LMT", :until -3225225600000}
      {:offset -33535000,
       :rule "-",
       :format "LMT",
       :until -2188987200000}
      {:offset -32400000, :rule "-", :format "YST", :until -883612800000}
      {:offset -32400000,
       :rule "US",
       :format "Y%sT",
       :until -757382400000}
      {:offset -32400000, :rule "-", :format "YST", :until -31536000000}
      {:offset -32400000,
       :rule "US",
       :format "Y%sT",
       :until 438998400000}]},
    "Africa/Kampala" "Africa/Nairobi",
    "Europe/Copenhagen"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 315532800000},
     :history
     [{:offset 3020000, :rule "-", :format "LMT", :until -2524521600000}
      {:offset 3020000, :rule "-", :format "CMT", :until -2398291200000}
      {:offset 3600000,
       :rule "Denmark",
       :format "CE%sT",
       :until -857246980000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -781048800000}
      {:offset 3600000,
       :rule "Denmark",
       :format "CE%sT",
       :until 315532800000}]},
    "Asia/Novosibirsk"
    {:current
     {:offset 25200000, :rule "-", :format "+07", :from 1469358000000},
     :history
     [{:offset 19900000, :rule "-", :format "LMT", :until -1579478400000}
      {:offset 21600000, :rule "-", :format "+06", :until -1247529600000}
      {:offset 25200000,
       :rule "Russia",
       :format "+07/+08",
       :until 670413600000}
      {:offset 21600000,
       :rule "Russia",
       :format "+06/+07",
       :until 695815200000}
      {:offset 25200000,
       :rule "Russia",
       :format "+07/+08",
       :until 738115200000}
      {:offset 21600000,
       :rule "Russia",
       :format "+06/+07",
       :until 1301223600000}
      {:offset 25200000, :rule "-", :format "+07", :until 1414314000000}
      {:offset 21600000,
       :rule "-",
       :format "+06",
       :until 1469358000000}]},
    "Europe/Monaco"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 220924800000},
     :history
     [{:offset 1772000, :rule "-", :format "LMT", :until -2486678400000}
      {:offset 561000, :rule "-", :format "PMT", :until -1855958400000}
      {:offset 0, :rule "France", :format "WE%sT", :until -766616400000}
      {:offset 3600000,
       :rule "France",
       :format "CE%sT",
       :until 220924800000}]},
    "EST5EDT"
    {:current {:offset -18000000, :rule "US", :format "E%sT", :from nil},
     :history []},
    "America/Chicago"
    {:current
     {:offset -21600000, :rule "US", :format "C%sT", :from -94694400000},
     :history
     [{:offset -21036000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -1577923200000}
      {:offset -21600000,
       :rule "Chicago",
       :format "C%sT",
       :until -1067810400000}
      {:offset -18000000,
       :rule "-",
       :format "EST",
       :until -1045432800000}
      {:offset -21600000,
       :rule "Chicago",
       :format "C%sT",
       :until -883612800000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -757382400000}
      {:offset -21600000,
       :rule "Chicago",
       :format "C%sT",
       :until -94694400000}]},
    "America/Godthab"
    {:current
     {:offset -10800000,
      :rule "EU",
      :format "-03/-02",
      :from 323834400000},
     :history
     [{:offset -12416000,
       :rule "-",
       :format "LMT",
       :until -1686096000000}
      {:offset -10800000,
       :rule "-",
       :format "-03",
       :until 323834400000}]},
    "Asia/Singapore"
    {:current
     {:offset 28800000, :rule "-", :format "+08", :from 378691200000},
     :history
     [{:offset 24925000, :rule "-", :format "LMT", :until -2177452800000}
      {:offset 24925000, :rule "-", :format "SMT", :until -2038176000000}
      {:offset 25200000, :rule "-", :format "+07", :until -1167609600000}
      {:offset 25200000,
       :rule 1200000,
       :format "+0720",
       :until -1073001600000}
      {:offset 26400000,
       :rule "-",
       :format "+0720",
       :until -894153600000}
      {:offset 27000000,
       :rule "-",
       :format "+0730",
       :until -879638400000}
      {:offset 32400000, :rule "-", :format "+09", :until -766972800000}
      {:offset 27000000,
       :rule "-",
       :format "+0730",
       :until 378691200000}]},
    "America/Lower_Princes" "America/Curacao",
    "Africa/Accra"
    {:current
     {:offset 0,
      :rule "Ghana",
      :format "GMT/+0020",
      :from -1640995200000},
     :history
     [{:offset -52000,
       :rule "-",
       :format "LMT",
       :until -1640995200000}]},
    "America/Asuncion"
    {:current
     {:offset -14400000,
      :rule "Para",
      :format "-04/-03",
      :from 134006400000},
     :history
     [{:offset -13840000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -13840000,
       :rule "-",
       :format "AMT",
       :until -1206403200000}
      {:offset -14400000, :rule "-", :format "-04", :until 86745600000}
      {:offset -10800000,
       :rule "-",
       :format "-03",
       :until 134006400000}]},
    "America/Thunder_Bay"
    {:current
     {:offset -18000000,
      :rule "Canada",
      :format "E%sT",
      :from 126230400000},
     :history
     [{:offset -21420000,
       :rule "-",
       :format "LMT",
       :until -2366755200000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until -1893456000000}
      {:offset -18000000, :rule "-", :format "EST", :until -883612800000}
      {:offset -18000000, :rule "Canada", :format "E%sT", :until 0}
      {:offset -18000000,
       :rule "Toronto",
       :format "E%sT",
       :until 94694400000}
      {:offset -18000000,
       :rule "-",
       :format "EST",
       :until 126230400000}]},
    "America/Bahia"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1350777600000},
     :history
     [{:offset -9244000, :rule "-", :format "LMT", :until -1767225600000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 1064361600000}
      {:offset -10800000, :rule "-", :format "-03", :until 1318723200000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 1350777600000}]},
    "Pacific/Majuro"
    {:current
     {:offset 43200000, :rule "-", :format "+12", :from -7948800000},
     :history
     [{:offset 41088000, :rule "-", :format "LMT", :until -2177452800000}
      {:offset 39600000, :rule "-", :format "+11", :until -7948800000}]},
    "Asia/Tomsk"
    {:current
     {:offset 25200000, :rule "-", :format "+07", :from 1464519600000},
     :history
     [{:offset 20391000, :rule "-", :format "LMT", :until -1578787200000}
      {:offset 21600000, :rule "-", :format "+06", :until -1247529600000}
      {:offset 25200000,
       :rule "Russia",
       :format "+07/+08",
       :until 670413600000}
      {:offset 21600000,
       :rule "Russia",
       :format "+06/+07",
       :until 695815200000}
      {:offset 25200000,
       :rule "Russia",
       :format "+07/+08",
       :until 1020222000000}
      {:offset 21600000,
       :rule "Russia",
       :format "+06/+07",
       :until 1301223600000}
      {:offset 25200000, :rule "-", :format "+07", :until 1414314000000}
      {:offset 21600000,
       :rule "-",
       :format "+06",
       :until 1464519600000}]},
    "Africa/Mbabane" "Africa/Johannesburg",
    "Pacific/Fakaofo"
    {:current
     {:offset 46800000, :rule "-", :format "+13", :from 1325203200000},
     :history
     [{:offset -41096000,
       :rule "-",
       :format "LMT",
       :until -2177452800000}
      {:offset -39600000,
       :rule "-",
       :format "-11",
       :until 1325203200000}]},
    "Africa/Addis_Ababa" "Africa/Nairobi",
    "America/Nassau"
    {:current
     {:offset -18000000, :rule "US", :format "E%sT", :from 189302400000},
     :history
     [{:offset -18570000,
       :rule "-",
       :format "LMT",
       :until -1825113600000}
      {:offset -18000000,
       :rule "Bahamas",
       :format "E%sT",
       :until 189302400000}]},
    "Asia/Makassar"
    {:current
     {:offset 28800000, :rule "-", :format "WITA", :from -766022400000},
     :history
     [{:offset 28656000, :rule "-", :format "LMT", :until -1577923200000}
      {:offset 28656000, :rule "-", :format "MMT", :until -1172880000000}
      {:offset 28800000, :rule "-", :format "+08", :until -880243200000}
      {:offset 32400000,
       :rule "-",
       :format "+09",
       :until -766022400000}]},
    "Europe/Nicosia" "Asia/Nicosia",
    "America/Whitehorse"
    {:current
     {:offset -28800000,
      :rule "Canada",
      :format "P%sT",
      :from 315532800000},
     :history
     [{:offset -32412000,
       :rule "-",
       :format "LMT",
       :until -2189030400000}
      {:offset -32400000,
       :rule "NT_YK",
       :format "Y%sT",
       :until -81993600000}
      {:offset -28800000,
       :rule "NT_YK",
       :format "P%sT",
       :until 315532800000}]},
    "Asia/Urumqi"
    {:current
     {:offset 21600000, :rule "-", :format "+06", :from -1325462400000},
     :history
     [{:offset 21020000,
       :rule "-",
       :format "LMT",
       :until -1325462400000}]},
    "Asia/Famagusta"
    {:current
     {:offset 7200000,
      :rule "EUAsia",
      :format "EE%sT",
      :from 1509242400000},
     :history
     [{:offset 8148000, :rule "-", :format "LMT", :until -1518912000000}
      {:offset 7200000,
       :rule "Cyprus",
       :format "EE%sT",
       :until 904608000000}
      {:offset 7200000,
       :rule "EUAsia",
       :format "EE%sT",
       :until 1473292800000}
      {:offset 10800000,
       :rule "-",
       :format "+03",
       :until 1509242400000}]},
    "Asia/Samarkand"
    {:current
     {:offset 18000000, :rule "-", :format "+05", :from 694224000000},
     :history
     [{:offset 16073000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 14400000, :rule "-", :format "+04", :until -1247529600000}
      {:offset 18000000, :rule "-", :format "+05", :until 354931200000}
      {:offset 18000000,
       :rule 3600000,
       :format "+06",
       :until 370742400000}
      {:offset 21600000, :rule "-", :format "+06", :until 386467200000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 694224000000}]},
    "Pacific/Niue"
    {:current
     {:offset -39600000, :rule "-", :format "-11", :from 276048000000},
     :history
     [{:offset -40780000,
       :rule "-",
       :format "LMT",
       :until -2177452800000}
      {:offset -40800000,
       :rule "-",
       :format "-1120",
       :until -599616000000}
      {:offset -41400000,
       :rule "-",
       :format "-1130",
       :until 276048000000}]},
    "Australia/Perth"
    {:current
     {:offset 28800000,
      :rule "AW",
      :format "AW%sT",
      :from -836438400000},
     :history
     [{:offset 27804000, :rule "-", :format "LMT", :until -2337897600000}
      {:offset 28800000,
       :rule "Aus",
       :format "AW%sT",
       :until -836438400000}]},
    "Africa/Bissau"
    {:current {:offset 0, :rule "-", :format "GMT", :from 157766400000},
     :history
     [{:offset -3740000, :rule "-", :format "LMT", :until -1830384000000}
      {:offset -3600000,
       :rule "-",
       :format "-01",
       :until 157766400000}]},
    "Asia/Thimphu"
    {:current
     {:offset 21600000, :rule "-", :format "+06", :from 560044800000},
     :history
     [{:offset 21516000, :rule "-", :format "LMT", :until -706320000000}
      {:offset 19800000,
       :rule "-",
       :format "+0530",
       :until 560044800000}]},
    "America/Antigua" "America/Port_of_Spain",
    "Africa/Khartoum"
    {:current
     {:offset 7200000, :rule "-", :format "CAT", :from 1509494400000},
     :history
     [{:offset 7808000, :rule "-", :format "LMT", :until -1230768000000}
      {:offset 7200000,
       :rule "Sudan",
       :format "CA%sT",
       :until 947937600000}
      {:offset 10800000,
       :rule "-",
       :format "EAT",
       :until 1509494400000}]},
    "America/Guyana"
    {:current
     {:offset -14400000, :rule "-", :format "-04", :from 662688000000},
     :history
     [{:offset -13960000,
       :rule "-",
       :format "LMT",
       :until -1730592000000}
      {:offset -13500000,
       :rule "-",
       :format "-0345",
       :until 175996800000}
      {:offset -10800000,
       :rule "-",
       :format "-03",
       :until 662688000000}]},
    "America/Puerto_Rico"
    {:current
     {:offset -14400000, :rule "-", :format "AST", :from -757382400000},
     :history
     [{:offset -15865000,
       :rule "-",
       :format "LMT",
       :until -2233094400000}
      {:offset -14400000, :rule "-", :format "AST", :until -873072000000}
      {:offset -14400000,
       :rule "US",
       :format "A%sT",
       :until -757382400000}]},
    "Pacific/Apia"
    {:current
     {:offset 46800000,
      :rule "WS",
      :format "+13/+14",
      :from 1325203200000},
     :history
     [{:offset 45184000, :rule "-", :format "LMT", :until -2445379200000}
      {:offset -41216000,
       :rule "-",
       :format "LMT",
       :until -1861920000000}
      {:offset -41400000,
       :rule "-",
       :format "-1130",
       :until -631152000000}
      {:offset -39600000,
       :rule "WS",
       :format "-11/-10",
       :until 1325203200000}]},
    "America/Indiana/Winamac"
    {:current
     {:offset -18000000,
      :rule "US",
      :format "E%sT",
      :from 1173578400000},
     :history
     [{:offset -20785000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -757382400000}
      {:offset -21600000,
       :rule "Pulaski",
       :format "C%sT",
       :until -273708000000}
      {:offset -18000000, :rule "-", :format "EST", :until -31536000000}
      {:offset -18000000, :rule "US", :format "E%sT", :until 31536000000}
      {:offset -18000000, :rule "-", :format "EST", :until 1143943200000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until 1173578400000}]},
    "Pacific/Galapagos"
    {:current
     {:offset -21600000,
      :rule "Ecuador",
      :format "-06/-05",
      :from 504921600000},
     :history
     [{:offset -21504000,
       :rule "-",
       :format "LMT",
       :until -1230768000000}
      {:offset -18000000,
       :rule "-",
       :format "-05",
       :until 504921600000}]},
    "Europe/Skopje" "Europe/Belgrade",
    "Europe/Istanbul"
    {:current
     {:offset 10800000, :rule "-", :format "+03", :from 1473206400000},
     :history
     [{:offset 6952000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 7016000, :rule "-", :format "IMT", :until -1869868800000}
      {:offset 7200000,
       :rule "Turkey",
       :format "EE%sT",
       :until 277257600000}
      {:offset 10800000,
       :rule "Turkey",
       :format "+03/+04",
       :until 482803200000}
      {:offset 7200000,
       :rule "Turkey",
       :format "EE%sT",
       :until 1167609600000}
      {:offset 7200000,
       :rule "EU",
       :format "EE%sT",
       :until 1301194800000}
      {:offset 7200000, :rule "-", :format "EET", :until 1301281200000}
      {:offset 7200000,
       :rule "EU",
       :format "EE%sT",
       :until 1396148400000}
      {:offset 7200000, :rule "-", :format "EET", :until 1396234800000}
      {:offset 7200000,
       :rule "EU",
       :format "EE%sT",
       :until 1445738400000}
      {:offset 7200000,
       :rule 3600000,
       :format "EEST",
       :until 1446948000000}
      {:offset 7200000,
       :rule "EU",
       :format "EE%sT",
       :until 1473206400000}]},
    "Asia/Jerusalem"
    {:current
     {:offset 7200000,
      :rule "Zion",
      :format "I%sT",
      :from -1640995200000},
     :history
     [{:offset 8454000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 8440000,
       :rule "-",
       :format "JMT",
       :until -1640995200000}]},
    "Asia/Magadan"
    {:current
     {:offset 39600000, :rule "-", :format "+11", :from 1461513600000},
     :history
     [{:offset 36192000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 36000000, :rule "-", :format "+10", :until -1247529600000}
      {:offset 39600000,
       :rule "Russia",
       :format "+11/+12",
       :until 670428000000}
      {:offset 36000000,
       :rule "Russia",
       :format "+10/+11",
       :until 695829600000}
      {:offset 39600000,
       :rule "Russia",
       :format "+11/+12",
       :until 1301234400000}
      {:offset 43200000, :rule "-", :format "+12", :until 1414332000000}
      {:offset 36000000,
       :rule "-",
       :format "+10",
       :until 1461513600000}]},
    "America/North_Dakota/Beulah"
    {:current
     {:offset -21600000,
      :rule "US",
      :format "C%sT",
      :from 1289095200000},
     :history
     [{:offset -24427000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -25200000,
       :rule "US",
       :format "M%sT",
       :until 1289095200000}]},
    "America/Guadeloupe" "America/Port_of_Spain",
    "America/St_Lucia" "America/Port_of_Spain",
    "America/Mexico_City"
    {:current
     {:offset -21600000,
      :rule "Mexico",
      :format "C%sT",
      :from 1014163200000},
     :history
     [{:offset -23796000,
       :rule "-",
       :format "LMT",
       :until -1514764800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1343091600000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until -1234828800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1220317200000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until -1207180800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1191369600000}
      {:offset -21600000,
       :rule "Mexico",
       :format "C%sT",
       :until 1001815200000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until 1014163200000}]},
    "Africa/Johannesburg"
    {:current
     {:offset 7200000, :rule "SA", :format "SAST", :from -2109283200000},
     :history
     [{:offset 6720000, :rule "-", :format "LMT", :until -2458166400000}
      {:offset 5400000,
       :rule "-",
       :format "SAST",
       :until -2109283200000}]},
    "Africa/Bamako" "Africa/Abidjan",
    "America/Toronto"
    {:current
     {:offset -18000000,
      :rule "Canada",
      :format "E%sT",
      :from 126230400000},
     :history
     [{:offset -19052000,
       :rule "-",
       :format "LMT",
       :until -2366755200000}
      {:offset -18000000,
       :rule "Canada",
       :format "E%sT",
       :until -1609459200000}
      {:offset -18000000,
       :rule "Toronto",
       :format "E%sT",
       :until -880246800000}
      {:offset -18000000,
       :rule "Canada",
       :format "E%sT",
       :until -757382400000}
      {:offset -18000000,
       :rule "Toronto",
       :format "E%sT",
       :until 126230400000}]},
    "America/Managua"
    {:current
     {:offset -21600000,
      :rule "Nic",
      :format "C%sT",
      :from 852076800000},
     :history
     [{:offset -20708000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -20712000,
       :rule "-",
       :format "MMT",
       :until -1121126400000}
      {:offset -21600000, :rule "-", :format "CST", :until 105062400000}
      {:offset -18000000, :rule "-", :format "EST", :until 161740800000}
      {:offset -21600000,
       :rule "Nic",
       :format "C%sT",
       :until 694238400000}
      {:offset -18000000, :rule "-", :format "EST", :until 717292800000}
      {:offset -21600000, :rule "-", :format "CST", :until 725846400000}
      {:offset -18000000,
       :rule "-",
       :format "EST",
       :until 852076800000}]},
    "America/Belem"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 590025600000},
     :history
     [{:offset -11636000,
       :rule "-",
       :format "LMT",
       :until -1767225600000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 590025600000}]},
    "Europe/Lisbon"
    {:current
     {:offset 0, :rule "EU", :format "WE%sT", :from 828241200000},
     :history
     [{:offset -2205000, :rule "-", :format "LMT", :until -2713910400000}
      {:offset -2205000, :rule "-", :format "LMT", :until -1830380400000}
      {:offset 0, :rule "Port", :format "WE%sT", :until -118274400000}
      {:offset 3600000, :rule "-", :format "CET", :until 212547600000}
      {:offset 0, :rule "Port", :format "WE%sT", :until 433306800000}
      {:offset 0, :rule "W-Eur", :format "WE%sT", :until 717559200000}
      {:offset 3600000,
       :rule "EU",
       :format "CE%sT",
       :until 828241200000}]},
    "America/Juneau"
    {:current
     {:offset -32400000,
      :rule "US",
      :format "AK%sT",
      :from 438998400000},
     :history
     [{:offset 54139000, :rule "-", :format "LMT", :until -3225225600000}
      {:offset -32261000,
       :rule "-",
       :format "LMT",
       :until -2188987200000}
      {:offset -28800000, :rule "-", :format "PST", :until -883612800000}
      {:offset -28800000,
       :rule "US",
       :format "P%sT",
       :until -757382400000}
      {:offset -28800000, :rule "-", :format "PST", :until -31536000000}
      {:offset -28800000,
       :rule "US",
       :format "P%sT",
       :until 325648800000}
      {:offset -32400000,
       :rule "US",
       :format "Y%sT",
       :until 341373600000}
      {:offset -28800000,
       :rule "US",
       :format "P%sT",
       :until 436327200000}
      {:offset -32400000,
       :rule "US",
       :format "Y%sT",
       :until 438998400000}]},
    "Europe/Minsk"
    {:current
     {:offset 10800000, :rule "-", :format "+03", :from 1301209200000},
     :history
     [{:offset 6616000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 6600000, :rule "-", :format "MMT", :until -1441152000000}
      {:offset 7200000, :rule "-", :format "EET", :until -1247529600000}
      {:offset 10800000, :rule "-", :format "MSK", :until -899769600000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -804643200000}
      {:offset 10800000,
       :rule "Russia",
       :format "MSK/MSD",
       :until 631152000000}
      {:offset 10800000, :rule "-", :format "MSK", :until 670402800000}
      {:offset 7200000,
       :rule "Russia",
       :format "EE%sT",
       :until 1301209200000}]},
    "America/Indiana/Tell_City"
    {:current
     {:offset -21600000,
      :rule "US",
      :format "C%sT",
      :from 1143943200000},
     :history
     [{:offset -20823000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -757382400000}
      {:offset -21600000,
       :rule "Perry",
       :format "C%sT",
       :until -179359200000}
      {:offset -18000000, :rule "-", :format "EST", :until -31536000000}
      {:offset -18000000, :rule "US", :format "E%sT", :until 31536000000}
      {:offset -18000000,
       :rule "-",
       :format "EST",
       :until 1143943200000}]},
    "Europe/Moscow"
    {:current
     {:offset 10800000, :rule "-", :format "MSK", :from 1414303200000},
     :history
     [{:offset 9017000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 9017000, :rule "-", :format "MMT", :until -1688256000000}
      {:offset 9079000,
       :rule "Russia",
       :format "%s",
       :until -1593817200000}
      {:offset 10800000,
       :rule "Russia",
       :format "%s",
       :until -1522713600000}
      {:offset 10800000,
       :rule "Russia",
       :format "MSK/MSD",
       :until -1491177600000}
      {:offset 7200000, :rule "-", :format "EET", :until -1247529600000}
      {:offset 10800000,
       :rule "Russia",
       :format "MSK/MSD",
       :until 670399200000}
      {:offset 7200000,
       :rule "Russia",
       :format "EE%sT",
       :until 695800800000}
      {:offset 10800000,
       :rule "Russia",
       :format "MSK/MSD",
       :until 1301205600000}
      {:offset 14400000,
       :rule "-",
       :format "MSK",
       :until 1414303200000}]},
    "Pacific/Kosrae"
    {:current
     {:offset 39600000, :rule "-", :format "+11", :from 915148800000},
     :history
     [{:offset 39116000, :rule "-", :format "LMT", :until -2177452800000}
      {:offset 39600000, :rule "-", :format "+11", :until -7948800000}
      {:offset 43200000,
       :rule "-",
       :format "+12",
       :until 915148800000}]},
    "Europe/Ulyanovsk"
    {:current
     {:offset 14400000, :rule "-", :format "+04", :from 1459065600000},
     :history
     [{:offset 11616000, :rule "-", :format "LMT", :until -1593820800000}
      {:offset 10800000, :rule "-", :format "+03", :until -1247529600000}
      {:offset 14400000,
       :rule "Russia",
       :format "+04/+05",
       :until 606898800000}
      {:offset 10800000,
       :rule "Russia",
       :format "+03/+04",
       :until 670406400000}
      {:offset 7200000,
       :rule "Russia",
       :format "+02/+03",
       :until 695800800000}
      {:offset 10800000,
       :rule "Russia",
       :format "+03/+04",
       :until 1301205600000}
      {:offset 14400000, :rule "-", :format "+04", :until 1414303200000}
      {:offset 10800000,
       :rule "-",
       :format "+03",
       :until 1459065600000}]},
    "Africa/Lagos"
    {:current
     {:offset 3600000, :rule "-", :format "WAT", :from -1588464000000},
     :history
     [{:offset 816000,
       :rule "-",
       :format "LMT",
       :until -1588464000000}]},
    "America/Guayaquil"
    {:current
     {:offset -18000000,
      :rule "Ecuador",
      :format "-05/-04",
      :from -1230768000000},
     :history
     [{:offset -19160000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -18840000,
       :rule "-",
       :format "QMT",
       :until -1230768000000}]},
    "America/Argentina/Cordoba"
    {:current
     {:offset -10800000,
      :rule "Arg",
      :format "-03/-02",
      :from 952041600000},
     :history
     [{:offset -15408000,
       :rule "-",
       :format "LMT",
       :until -2372112000000}
      {:offset -15408000,
       :rule "-",
       :format "CMT",
       :until -1567468800000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1233446400000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until -7603200000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 667958400000}
      {:offset -14400000, :rule "-", :format "-04", :until 687916800000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 938908800000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until 952041600000}]},
    "Africa/Windhoek"
    {:current
     {:offset 7200000,
      :rule "Namibia",
      :format "%s",
      :from 637977600000},
     :history
     [{:offset 4104000, :rule "-", :format "LMT", :until -2458166400000}
      {:offset 5400000,
       :rule "-",
       :format "+0130",
       :until -2109283200000}
      {:offset 7200000, :rule "-", :format "SAST", :until -860968800000}
      {:offset 7200000,
       :rule 3600000,
       :format "SAST",
       :until -845244000000}
      {:offset 7200000,
       :rule "-",
       :format "SAST",
       :until 637977600000}]},
    "Europe/Rome"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 315532800000},
     :history
     [{:offset 2996000, :rule "-", :format "LMT", :until -3259094400000}
      {:offset 2996000, :rule "-", :format "RMT", :until -2403562260000}
      {:offset 3600000,
       :rule "Italy",
       :format "CE%sT",
       :until -830304000000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -807148800000}
      {:offset 3600000,
       :rule "Italy",
       :format "CE%sT",
       :until 315532800000}]},
    "Asia/Krasnoyarsk"
    {:current
     {:offset 25200000, :rule "-", :format "+07", :from 1414317600000},
     :history
     [{:offset 22286000, :rule "-", :format "LMT", :until -1577491200000}
      {:offset 21600000, :rule "-", :format "+06", :until -1247529600000}
      {:offset 25200000,
       :rule "Russia",
       :format "+07/+08",
       :until 670413600000}
      {:offset 21600000,
       :rule "Russia",
       :format "+06/+07",
       :until 695815200000}
      {:offset 25200000,
       :rule "Russia",
       :format "+07/+08",
       :until 1301220000000}
      {:offset 28800000,
       :rule "-",
       :format "+08",
       :until 1414317600000}]},
    "America/Glace_Bay"
    {:current
     {:offset -14400000,
      :rule "Canada",
      :format "A%sT",
      :from 126230400000},
     :history
     [{:offset -14388000,
       :rule "-",
       :format "LMT",
       :until -2131660800000}
      {:offset -14400000,
       :rule "Canada",
       :format "A%sT",
       :until -536457600000}
      {:offset -14400000,
       :rule "Halifax",
       :format "A%sT",
       :until -504921600000}
      {:offset -14400000, :rule "-", :format "AST", :until 63072000000}
      {:offset -14400000,
       :rule "Halifax",
       :format "A%sT",
       :until 126230400000}]},
    "Asia/Karachi"
    {:current
     {:offset 18000000,
      :rule "Pakistan",
      :format "PK%sT",
      :from 38793600000},
     :history
     [{:offset 16092000, :rule "-", :format "LMT", :until -1988150400000}
      {:offset 19800000,
       :rule "-",
       :format "+0530",
       :until -862617600000}
      {:offset 19800000,
       :rule 3600000,
       :format "+0630",
       :until -764121600000}
      {:offset 19800000,
       :rule "-",
       :format "+0530",
       :until -576115200000}
      {:offset 18000000, :rule "-", :format "+05", :until 38793600000}]},
    "Europe/Busingen" "Europe/Zurich",
    "Asia/Aden" "Asia/Riyadh",
    "America/Mazatlan"
    {:current
     {:offset -25200000, :rule "Mexico", :format "M%sT", :from 0},
     :history
     [{:offset -25540000,
       :rule "-",
       :format "LMT",
       :until -1514851200000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1343091600000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until -1234828800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1220317200000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until -1207180800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1191369600000}
      {:offset -21600000, :rule "-", :format "CST", :until -873849600000}
      {:offset -25200000, :rule "-", :format "MST", :until -661564800000}
      {:offset -28800000, :rule "-", :format "PST", :until 0}]},
    "America/Winnipeg"
    {:current
     {:offset -21600000,
      :rule "Canada",
      :format "C%sT",
      :from 1136073600000},
     :history
     [{:offset -23316000,
       :rule "-",
       :format "LMT",
       :until -2602281600000}
      {:offset -21600000,
       :rule "Winn",
       :format "C%sT",
       :until 1136073600000}]},
    "Europe/Chisinau"
    {:current
     {:offset 7200000,
      :rule "Moldova",
      :format "EE%sT",
      :from 852076800000},
     :history
     [{:offset 6920000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 6900000, :rule "-", :format "CMT", :until -1637107200000}
      {:offset 6264000, :rule "-", :format "BMT", :until -1213142400000}
      {:offset 7200000,
       :rule "Romania",
       :format "EE%sT",
       :until -927158400000}
      {:offset 7200000,
       :rule 3600000,
       :format "EEST",
       :until -898128000000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -800150400000}
      {:offset 10800000,
       :rule "Russia",
       :format "MSK/MSD",
       :until 641959200000}
      {:offset 7200000,
       :rule "Russia",
       :format "EE%sT",
       :until 694224000000}
      {:offset 7200000,
       :rule "E-Eur",
       :format "EE%sT",
       :until 852076800000}]},
    "America/Argentina/Buenos_Aires"
    {:current
     {:offset -10800000,
      :rule "Arg",
      :format "-03/-02",
      :from 952041600000},
     :history
     [{:offset -14028000,
       :rule "-",
       :format "LMT",
       :until -2372112000000}
      {:offset -15408000,
       :rule "-",
       :format "CMT",
       :until -1567468800000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1233446400000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until -7603200000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 938908800000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until 952041600000}]},
    "Europe/Riga"
    {:current
     {:offset 7200000, :rule "EU", :format "EE%sT", :from 978393600000},
     :history
     [{:offset 5794000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 5794000, :rule "-", :format "RMT", :until -1632002400000}
      {:offset 5794000,
       :rule 3600000,
       :format "LST",
       :until -1618693200000}
      {:offset 5794000, :rule "-", :format "RMT", :until -1601676000000}
      {:offset 5794000,
       :rule 3600000,
       :format "LST",
       :until -1597266000000}
      {:offset 5794000, :rule "-", :format "RMT", :until -1377302400000}
      {:offset 7200000, :rule "-", :format "EET", :until -928022400000}
      {:offset 10800000, :rule "-", :format "MSK", :until -899510400000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -795830400000}
      {:offset 10800000,
       :rule "Russia",
       :format "MSK/MSD",
       :until 606884400000}
      {:offset 7200000,
       :rule 3600000,
       :format "EEST",
       :until 622612800000}
      {:offset 7200000,
       :rule "Latvia",
       :format "EE%sT",
       :until 853804800000}
      {:offset 7200000, :rule "EU", :format "EE%sT", :until 951782400000}
      {:offset 7200000, :rule "-", :format "EET", :until 978393600000}]},
    "Pacific/Pitcairn"
    {:current
     {:offset -28800000, :rule "-", :format "-08", :from 893635200000},
     :history
     [{:offset -31220000,
       :rule "-",
       :format "LMT",
       :until -2177452800000}
      {:offset -30600000,
       :rule "-",
       :format "-0830",
       :until 893635200000}]},
    "Pacific/Guam"
    {:current
     {:offset 36000000, :rule "-", :format "ChST", :from 977529600000},
     :history
     [{:offset -51660000,
       :rule "-",
       :format "LMT",
       :until -3944678400000}
      {:offset 34740000, :rule "-", :format "LMT", :until -2177452800000}
      {:offset 36000000,
       :rule "-",
       :format "GST",
       :until 977529600000}]},
    "Indian/Reunion"
    {:current
     {:offset 14400000, :rule "-", :format "+04", :from -1848873600000},
     :history
     [{:offset 13312000,
       :rule "-",
       :format "LMT",
       :until -1848873600000}]},
    "Pacific/Bougainville"
    {:current
     {:offset 39600000, :rule "-", :format "+11", :from 1419732000000},
     :history
     [{:offset 37336000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 35312000,
       :rule "-",
       :format "PMMT",
       :until -2366755200000}
      {:offset 36000000, :rule "-", :format "+10", :until -867974400000}
      {:offset 32400000, :rule "-", :format "+09", :until -768873600000}
      {:offset 36000000,
       :rule "-",
       :format "+10",
       :until 1419732000000}]},
    "Africa/Malabo" "Africa/Lagos",
    "America/New_York"
    {:current
     {:offset -18000000, :rule "US", :format "E%sT", :from -94694400000},
     :history
     [{:offset -17762000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -18000000,
       :rule "US",
       :format "E%sT",
       :until -1577923200000}
      {:offset -18000000,
       :rule "NYC",
       :format "E%sT",
       :until -883612800000}
      {:offset -18000000,
       :rule "US",
       :format "E%sT",
       :until -757382400000}
      {:offset -18000000,
       :rule "NYC",
       :format "E%sT",
       :until -94694400000}]},
    "Africa/Tripoli"
    {:current
     {:offset 7200000, :rule "-", :format "EET", :from 1382666400000},
     :history
     [{:offset 3164000, :rule "-", :format "LMT", :until -1577923200000}
      {:offset 3600000,
       :rule "Libya",
       :format "CE%sT",
       :until -347155200000}
      {:offset 7200000, :rule "-", :format "EET", :until 378691200000}
      {:offset 3600000,
       :rule "Libya",
       :format "CE%sT",
       :until 641779200000}
      {:offset 7200000, :rule "-", :format "EET", :until 844041600000}
      {:offset 3600000,
       :rule "Libya",
       :format "CE%sT",
       :until 875923200000}
      {:offset 7200000, :rule "-", :format "EET", :until 1352512800000}
      {:offset 3600000,
       :rule "Libya",
       :format "CE%sT",
       :until 1382666400000}]},
    "Asia/Bangkok"
    {:current
     {:offset 25200000, :rule "-", :format "+07", :from -1570060800000},
     :history
     [{:offset 24124000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 24124000,
       :rule "-",
       :format "BMT",
       :until -1570060800000}]},
    "America/Atikokan"
    {:current
     {:offset -18000000, :rule "-", :format "EST", :from -765410400000},
     :history
     [{:offset -21988000,
       :rule "-",
       :format "LMT",
       :until -2366755200000}
      {:offset -21600000,
       :rule "Canada",
       :format "C%sT",
       :until -923270400000}
      {:offset -21600000,
       :rule 3600000,
       :format "CDT",
       :until -880250400000}
      {:offset -21600000,
       :rule "Canada",
       :format "C%sT",
       :until -765410400000}]},
    "America/Detroit"
    {:current
     {:offset -18000000, :rule "US", :format "E%sT", :from 167796000000},
     :history
     [{:offset -19931000,
       :rule "-",
       :format "LMT",
       :until -2051222400000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until -1724104800000}
      {:offset -18000000, :rule "-", :format "EST", :until -883612800000}
      {:offset -18000000,
       :rule "US",
       :format "E%sT",
       :until -757382400000}
      {:offset -18000000,
       :rule "Detroit",
       :format "E%sT",
       :until 94694400000}
      {:offset -18000000,
       :rule "US",
       :format "E%sT",
       :until 157766400000}
      {:offset -18000000,
       :rule "-",
       :format "EST",
       :until 167796000000}]},
    "Asia/Ashgabat"
    {:current
     {:offset 18000000, :rule "-", :format "+05", :from 695786400000},
     :history
     [{:offset 14012000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 14400000, :rule "-", :format "+04", :until -1247529600000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 670381200000}
      {:offset 14400000,
       :rule "RussiaAsia",
       :format "+04/+05",
       :until 695786400000}]},
    "America/Edmonton"
    {:current
     {:offset -25200000,
      :rule "Canada",
      :format "M%sT",
      :from 536457600000},
     :history
     [{:offset -27232000,
       :rule "-",
       :format "LMT",
       :until -1998691200000}
      {:offset -25200000,
       :rule "Edm",
       :format "M%sT",
       :until 536457600000}]},
    "Europe/Podgorica" "Europe/Belgrade",
    "Europe/Saratov"
    {:current
     {:offset 14400000, :rule "-", :format "+04", :from 1480834800000},
     :history
     [{:offset 11058000, :rule "-", :format "LMT", :until -1593820800000}
      {:offset 10800000, :rule "-", :format "+03", :until -1247529600000}
      {:offset 14400000,
       :rule "Russia",
       :format "+04/+05",
       :until 575449200000}
      {:offset 10800000,
       :rule "Russia",
       :format "+03/+04",
       :until 670406400000}
      {:offset 14400000, :rule "-", :format "+04", :until 701852400000}
      {:offset 10800000,
       :rule "Russia",
       :format "+03/+04",
       :until 1301212800000}
      {:offset 14400000, :rule "-", :format "+04", :until 1414303200000}
      {:offset 10800000,
       :rule "-",
       :format "+03",
       :until 1480834800000}]},
    "Asia/Damascus"
    {:current
     {:offset 7200000,
      :rule "Syria",
      :format "EE%sT",
      :from -1577923200000},
     :history
     [{:offset 8712000,
       :rule "-",
       :format "LMT",
       :until -1577923200000}]},
    "Africa/Bujumbura" "Africa/Maputo",
    "Australia/Lord_Howe"
    {:current
     {:offset 37800000,
      :rule "LH",
      :format "+1030/+11",
      :from 489024000000},
     :history
     [{:offset 38180000, :rule "-", :format "LMT", :until -2364076800000}
      {:offset 36000000, :rule "-", :format "AEST", :until 352252800000}
      {:offset 37800000,
       :rule "LH",
       :format "+1030/+1130",
       :until 489024000000}]},
    "Africa/Bangui" "Africa/Lagos",
    "America/Kentucky/Monticello"
    {:current
     {:offset -18000000, :rule "US", :format "E%sT", :from 972784800000},
     :history
     [{:offset -20364000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until -757382400000}
      {:offset -21600000, :rule "-", :format "CST", :until -63158400000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until 972784800000}]},
    "Europe/Mariehamn" "Europe/Helsinki",
    "Australia/Eucla"
    {:current
     {:offset 31500000,
      :rule "AW",
      :format "+0845/+0945",
      :from -836438400000},
     :history
     [{:offset 30928000, :rule "-", :format "LMT", :until -2337897600000}
      {:offset 31500000,
       :rule "Aus",
       :format "+0845/+0945",
       :until -836438400000}]},
    "America/North_Dakota/New_Salem"
    {:current
     {:offset -21600000,
      :rule "US",
      :format "C%sT",
      :from 1067133600000},
     :history
     [{:offset -24339000,
       :rule "-",
       :format "LMT",
       :until -2717712000000}
      {:offset -25200000,
       :rule "US",
       :format "M%sT",
       :until 1067133600000}]},
    "Africa/Juba"
    {:current
     {:offset 10800000, :rule "-", :format "EAT", :from 947937600000},
     :history
     [{:offset 7588000, :rule "-", :format "LMT", :until -1230768000000}
      {:offset 7200000,
       :rule "Sudan",
       :format "CA%sT",
       :until 947937600000}]},
    "Africa/Gaborone" "Africa/Maputo",
    "Europe/Ljubljana" "Europe/Belgrade",
    "Australia/Lindeman"
    {:current
     {:offset 36000000,
      :rule "Holiday",
      :format "AE%sT",
      :from 709948800000},
     :history
     [{:offset 35756000, :rule "-", :format "LMT", :until -2366755200000}
      {:offset 36000000,
       :rule "Aus",
       :format "AE%sT",
       :until 31536000000}
      {:offset 36000000,
       :rule "AQ",
       :format "AE%sT",
       :until 709948800000}]},
    "Africa/Mogadishu" "Africa/Nairobi",
    "America/St_Thomas" "America/Port_of_Spain",
    "America/Havana"
    {:current
     {:offset -18000000,
      :rule "Cuba",
      :format "C%sT",
      :from -1402833600000},
     :history
     [{:offset -19768000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -19776000,
       :rule "-",
       :format "HMT",
       :until -1402833600000}]},
    "Asia/Ulaanbaatar"
    {:current
     {:offset 28800000,
      :rule "Mongol",
      :format "+08/+09",
      :from 252460800000},
     :history
     [{:offset 25652000, :rule "-", :format "LMT", :until -2032905600000}
      {:offset 25200000,
       :rule "-",
       :format "+07",
       :until 252460800000}]},
    "Europe/Vaduz" "Europe/Zurich",
    "America/Anchorage"
    {:current
     {:offset -32400000,
      :rule "US",
      :format "AK%sT",
      :from 438998400000},
     :history
     [{:offset 50424000, :rule "-", :format "LMT", :until -3225225600000}
      {:offset -35976000,
       :rule "-",
       :format "LMT",
       :until -2188987200000}
      {:offset -36000000, :rule "-", :format "AST", :until -883612800000}
      {:offset -36000000,
       :rule "US",
       :format "A%sT",
       :until -86918400000}
      {:offset -36000000, :rule "-", :format "AHST", :until -31536000000}
      {:offset -36000000,
       :rule "US",
       :format "AH%sT",
       :until 436327200000}
      {:offset -32400000,
       :rule "US",
       :format "Y%sT",
       :until 438998400000}]},
    "America/Iqaluit"
    {:current
     {:offset -18000000,
      :rule "Canada",
      :format "E%sT",
      :from 972784800000},
     :history
     [{:offset 0, :rule "-", :format "-00", :until -865296000000}
      {:offset -18000000,
       :rule "NT_YK",
       :format "E%sT",
       :until 941335200000}
      {:offset -21600000,
       :rule "Canada",
       :format "C%sT",
       :until 972784800000}]},
    "Asia/Riyadh"
    {:current
     {:offset 10800000, :rule "-", :format "+03", :from -719625600000},
     :history
     [{:offset 11212000,
       :rule "-",
       :format "LMT",
       :until -719625600000}]},
    "America/Vancouver"
    {:current
     {:offset -28800000,
      :rule "Canada",
      :format "P%sT",
      :from 536457600000},
     :history
     [{:offset -29548000,
       :rule "-",
       :format "LMT",
       :until -2713910400000}
      {:offset -28800000,
       :rule "Vanc",
       :format "P%sT",
       :until 536457600000}]},
    "America/Grenada" "America/Port_of_Spain",
    "America/Rio_Branco"
    {:current
     {:offset -18000000, :rule "-", :format "-05", :from 1384041600000},
     :history
     [{:offset -16272000,
       :rule "-",
       :format "LMT",
       :until -1767225600000}
      {:offset -18000000,
       :rule "Brazil",
       :format "-05/-04",
       :until 590025600000}
      {:offset -18000000, :rule "-", :format "-05", :until 1214265600000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until 1384041600000}]},
    "Europe/Bratislava" "Europe/Prague",
    "Pacific/Fiji"
    {:current
     {:offset 43200000,
      :rule "Fiji",
      :format "+12/+13",
      :from -1709942400000},
     :history
     [{:offset 42944000,
       :rule "-",
       :format "LMT",
       :until -1709942400000}]},
    "Asia/Omsk"
    {:current
     {:offset 21600000, :rule "-", :format "+06", :from 1414314000000},
     :history
     [{:offset 17610000, :rule "-", :format "LMT", :until -1582070400000}
      {:offset 18000000, :rule "-", :format "+05", :until -1247529600000}
      {:offset 21600000,
       :rule "Russia",
       :format "+06/+07",
       :until 670410000000}
      {:offset 18000000,
       :rule "Russia",
       :format "+05/+06",
       :until 695811600000}
      {:offset 21600000,
       :rule "Russia",
       :format "+06/+07",
       :until 1301216400000}
      {:offset 25200000,
       :rule "-",
       :format "+07",
       :until 1414314000000}]},
    "Pacific/Funafuti"
    {:current
     {:offset 43200000, :rule "-", :format "+12", :from -2177452800000},
     :history
     [{:offset 43012000,
       :rule "-",
       :format "LMT",
       :until -2177452800000}]},
    "America/Merida"
    {:current
     {:offset -21600000,
      :rule "Mexico",
      :format "C%sT",
      :from 407635200000},
     :history
     [{:offset -21508000,
       :rule "-",
       :format "LMT",
       :until -1514764800000}
      {:offset -21600000, :rule "-", :format "CST", :until 377913600000}
      {:offset -18000000,
       :rule "-",
       :format "EST",
       :until 407635200000}]},
    "Asia/Istanbul" "Europe/Istanbul",
    "America/Cayman" "America/Panama",
    "Pacific/Noumea"
    {:current
     {:offset 39600000,
      :rule "NC",
      :format "+11/+12",
      :from -1829347200000},
     :history
     [{:offset 39948000,
       :rule "-",
       :format "LMT",
       :until -1829347200000}]},
    "Indian/Mahe"
    {:current
     {:offset 14400000, :rule "-", :format "+04", :from -2006640000000},
     :history
     [{:offset 13308000,
       :rule "-",
       :format "LMT",
       :until -2006640000000}]},
    "Asia/Irkutsk"
    {:current
     {:offset 28800000, :rule "-", :format "+08", :from 1414321200000},
     :history
     [{:offset 25025000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 25025000, :rule "-", :format "IMT", :until -1575849600000}
      {:offset 25200000, :rule "-", :format "+07", :until -1247529600000}
      {:offset 28800000,
       :rule "Russia",
       :format "+08/+09",
       :until 670417200000}
      {:offset 25200000,
       :rule "Russia",
       :format "+07/+08",
       :until 695818800000}
      {:offset 28800000,
       :rule "Russia",
       :format "+08/+09",
       :until 1301223600000}
      {:offset 32400000,
       :rule "-",
       :format "+09",
       :until 1414321200000}]},
    "Antarctica/Palmer"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1480809600000},
     :history
     [{:offset 0, :rule "-", :format "-00", :until -157766400000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until -7603200000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 389059200000}
      {:offset -14400000,
       :rule "Chile",
       :format "-04/-03",
       :until 1480809600000}]},
    "America/Bahia_Banderas"
    {:current
     {:offset -21600000,
      :rule "Mexico",
      :format "C%sT",
      :from 1270346400000},
     :history
     [{:offset -25260000,
       :rule "-",
       :format "LMT",
       :until -1514851200000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1343091600000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until -1234828800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1220317200000}
      {:offset -21600000,
       :rule "-",
       :format "CST",
       :until -1207180800000}
      {:offset -25200000,
       :rule "-",
       :format "MST",
       :until -1191369600000}
      {:offset -21600000, :rule "-", :format "CST", :until -873849600000}
      {:offset -25200000, :rule "-", :format "MST", :until -661564800000}
      {:offset -28800000, :rule "-", :format "PST", :until 0}
      {:offset -25200000,
       :rule "Mexico",
       :format "M%sT",
       :until 1270346400000}]},
    "Europe/Budapest"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 338961600000},
     :history
     [{:offset 4580000, :rule "-", :format "LMT", :until -2500934400000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -1640995200000}
      {:offset 3600000,
       :rule "Hungary",
       :format "CE%sT",
       :until -906768000000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -788918400000}
      {:offset 3600000,
       :rule "Hungary",
       :format "CE%sT",
       :until 338961600000}]},
    "America/Regina"
    {:current
     {:offset -21600000, :rule "-", :format "CST", :from -305769600000},
     :history
     [{:offset -25116000,
       :rule "-",
       :format "LMT",
       :until -2030227200000}
      {:offset -25200000,
       :rule "Regina",
       :format "M%sT",
       :until -305769600000}]},
    "America/Halifax"
    {:current
     {:offset -14400000,
      :rule "Canada",
      :format "A%sT",
      :from 126230400000},
     :history
     [{:offset -15264000,
       :rule "-",
       :format "LMT",
       :until -2131660800000}
      {:offset -14400000,
       :rule "Halifax",
       :format "A%sT",
       :until -1640995200000}
      {:offset -14400000,
       :rule "Canada",
       :format "A%sT",
       :until -1609459200000}
      {:offset -14400000,
       :rule "Halifax",
       :format "A%sT",
       :until -880243200000}
      {:offset -14400000,
       :rule "Canada",
       :format "A%sT",
       :until -757382400000}
      {:offset -14400000,
       :rule "Halifax",
       :format "A%sT",
       :until 126230400000}]},
    "Asia/Anadyr"
    {:current
     {:offset 43200000, :rule "-", :format "+12", :from 1301241600000},
     :history
     [{:offset 42596000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 43200000, :rule "-", :format "+12", :until -1247529600000}
      {:offset 46800000,
       :rule "Russia",
       :format "+13/+14",
       :until 386514000000}
      {:offset 43200000,
       :rule "Russia",
       :format "+12/+13",
       :until 670438800000}
      {:offset 39600000,
       :rule "Russia",
       :format "+11/+12",
       :until 695833200000}
      {:offset 43200000,
       :rule "Russia",
       :format "+12/+13",
       :until 1269788400000}
      {:offset 39600000,
       :rule "Russia",
       :format "+11/+12",
       :until 1301241600000}]},
    "America/Cuiaba"
    {:current
     {:offset -14400000,
      :rule "Brazil",
      :format "-04/-03",
      :from 1096588800000},
     :history
     [{:offset -13460000,
       :rule "-",
       :format "LMT",
       :until -1767225600000}
      {:offset -14400000,
       :rule "Brazil",
       :format "-04/-03",
       :until 1064361600000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until 1096588800000}]},
    "Africa/Porto-Novo" "Africa/Lagos",
    "America/Argentina/Catamarca"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
     :history
     [{:offset -15788000,
       :rule "-",
       :format "LMT",
       :until -2372112000000}
      {:offset -15408000,
       :rule "-",
       :format "CMT",
       :until -1567468800000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1233446400000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until -7603200000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 667958400000}
      {:offset -14400000, :rule "-", :format "-04", :until 687916800000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 938908800000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until 952041600000}
      {:offset -10800000, :rule "-", :format "-03", :until 1086048000000}
      {:offset -14400000, :rule "-", :format "-04", :until 1087689600000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 1224288000000}]},
    "Indian/Mauritius"
    {:current
     {:offset 14400000,
      :rule "Mauritius",
      :format "+04/+05",
      :from -1988150400000},
     :history
     [{:offset 13800000,
       :rule "-",
       :format "LMT",
       :until -1988150400000}]},
    "Europe/Samara"
    {:current
     {:offset 14400000, :rule "-", :format "+04", :from 1301212800000},
     :history
     [{:offset 12020000, :rule "-", :format "LMT", :until -1593820800000}
      {:offset 10800000, :rule "-", :format "+03", :until -1247529600000}
      {:offset 14400000, :rule "-", :format "+04", :until -1102291200000}
      {:offset 14400000,
       :rule "Russia",
       :format "+04/+05",
       :until 606902400000}
      {:offset 10800000,
       :rule "Russia",
       :format "+03/+04",
       :until 670406400000}
      {:offset 7200000,
       :rule "Russia",
       :format "+02/+03",
       :until 686124000000}
      {:offset 10800000, :rule "-", :format "+03", :until 687927600000}
      {:offset 14400000,
       :rule "Russia",
       :format "+04/+05",
       :until 1269759600000}
      {:offset 10800000,
       :rule "Russia",
       :format "+03/+04",
       :until 1301212800000}]},
    "America/Eirunepe"
    {:current
     {:offset -18000000, :rule "-", :format "-05", :from 1384041600000},
     :history
     [{:offset -16768000,
       :rule "-",
       :format "LMT",
       :until -1767225600000}
      {:offset -18000000,
       :rule "Brazil",
       :format "-05/-04",
       :until 590025600000}
      {:offset -18000000, :rule "-", :format "-05", :until 749174400000}
      {:offset -18000000,
       :rule "Brazil",
       :format "-05/-04",
       :until 780192000000}
      {:offset -18000000, :rule "-", :format "-05", :until 1214265600000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until 1384041600000}]},
    "America/Fort_Nelson"
    {:current
     {:offset -25200000, :rule "-", :format "MST", :from 1425780000000},
     :history
     [{:offset -29447000,
       :rule "-",
       :format "LMT",
       :until -2713910400000}
      {:offset -28800000,
       :rule "Vanc",
       :format "P%sT",
       :until -757382400000}
      {:offset -28800000, :rule "-", :format "PST", :until -725846400000}
      {:offset -28800000,
       :rule "Vanc",
       :format "P%sT",
       :until 536457600000}
      {:offset -28800000,
       :rule "Canada",
       :format "P%sT",
       :until 1425780000000}]},
    "Africa/Asmara" "Africa/Nairobi",
    "Asia/Tashkent"
    {:current
     {:offset 18000000, :rule "-", :format "+05", :from 694224000000},
     :history
     [{:offset 16631000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 18000000, :rule "-", :format "+05", :until -1247529600000}
      {:offset 21600000,
       :rule "RussiaAsia",
       :format "+06/+07",
       :until 670381200000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 694224000000}]},
    "Africa/Maputo"
    {:current
     {:offset 7200000, :rule "-", :format "CAT", :from -2109283200000},
     :history
     [{:offset 7820000,
       :rule "-",
       :format "LMT",
       :until -2109283200000}]},
    "Pacific/Kiritimati"
    {:current
     {:offset 50400000, :rule "-", :format "+14", :from 788832000000},
     :history
     [{:offset -37760000,
       :rule "-",
       :format "LMT",
       :until -2177452800000}
      {:offset -38400000,
       :rule "-",
       :format "-1040",
       :until 307584000000}
      {:offset -36000000,
       :rule "-",
       :format "-10",
       :until 788832000000}]},
    "Asia/Qyzylorda"
    {:current
     {:offset 21600000, :rule "-", :format "+06", :from 1099213200000},
     :history
     [{:offset 15712000, :rule "-", :format "LMT", :until -1441152000000}
      {:offset 14400000, :rule "-", :format "+04", :until -1247529600000}
      {:offset 18000000, :rule "-", :format "+05", :until 354931200000}
      {:offset 18000000,
       :rule 3600000,
       :format "+06",
       :until 370742400000}
      {:offset 21600000, :rule "-", :format "+06", :until 386467200000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 670413600000}
      {:offset 14400000,
       :rule "RussiaAsia",
       :format "+04/+05",
       :until 686131200000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 695804400000}
      {:offset 21600000,
       :rule "RussiaAsia",
       :format "+06/+07",
       :until 701859600000}
      {:offset 18000000,
       :rule "RussiaAsia",
       :format "+05/+06",
       :until 1099213200000}]},
    "Pacific/Midway" "Pacific/Pago_Pago",
    "America/Kralendijk" "America/Curacao",
    "Asia/Kabul"
    {:current
     {:offset 16200000, :rule "-", :format "+0430", :from -788918400000},
     :history
     [{:offset 16608000, :rule "-", :format "LMT", :until -2524521600000}
      {:offset 14400000,
       :rule "-",
       :format "+04",
       :until -788918400000}]},
    "America/Port_of_Spain"
    {:current
     {:offset -14400000, :rule "-", :format "AST", :from -1825113600000},
     :history
     [{:offset -14764000,
       :rule "-",
       :format "LMT",
       :until -1825113600000}]},
    "Europe/Zurich"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 347155200000},
     :history
     [{:offset 2048000, :rule "-", :format "LMT", :until -3675196800000}
      {:offset 1786000, :rule "-", :format "BMT", :until -2385244800000}
      {:offset 3600000,
       :rule "Swiss",
       :format "CE%sT",
       :until 347155200000}]},
    "Asia/Kathmandu"
    {:current
     {:offset 20700000, :rule "-", :format "+0545", :from 504921600000},
     :history
     [{:offset 20476000, :rule "-", :format "LMT", :until -1577923200000}
      {:offset 19800000,
       :rule "-",
       :format "+0530",
       :until 504921600000}]},
    "Africa/Algiers"
    {:current
     {:offset 3600000, :rule "-", :format "CET", :from 357523200000},
     :history
     [{:offset 732000, :rule "-", :format "LMT", :until -2486678400000}
      {:offset 561000, :rule "-", :format "PMT", :until -1855958400000}
      {:offset 0, :rule "Algeria", :format "WE%sT", :until -942012000000}
      {:offset 3600000,
       :rule "Algeria",
       :format "CE%sT",
       :until -733276800000}
      {:offset 0, :rule "-", :format "WET", :until -439430400000}
      {:offset 3600000, :rule "-", :format "CET", :until -212025600000}
      {:offset 0, :rule "Algeria", :format "WE%sT", :until 246240000000}
      {:offset 3600000,
       :rule "Algeria",
       :format "CE%sT",
       :until 309744000000}
      {:offset 0,
       :rule "Algeria",
       :format "WE%sT",
       :until 357523200000}]},
    "America/Matamoros"
    {:current
     {:offset -21600000,
      :rule "US",
      :format "C%sT",
      :from 1262304000000},
     :history
     [{:offset -24000000,
       :rule "-",
       :format "LMT",
       :until -1514851200000}
      {:offset -21600000, :rule "-", :format "CST", :until 567993600000}
      {:offset -21600000,
       :rule "US",
       :format "C%sT",
       :until 599616000000}
      {:offset -21600000,
       :rule "Mexico",
       :format "C%sT",
       :until 1262304000000}]},
    "America/Araguaina"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 1377993600000},
     :history
     [{:offset -11568000,
       :rule "-",
       :format "LMT",
       :until -1767225600000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 653529600000}
      {:offset -10800000, :rule "-", :format "-03", :until 811036800000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 1064361600000}
      {:offset -10800000, :rule "-", :format "-03", :until 1350777600000}
      {:offset -10800000,
       :rule "Brazil",
       :format "-03/-02",
       :until 1377993600000}]},
    "America/Argentina/Tucuman"
    {:current
     {:offset -10800000,
      :rule "Arg",
      :format "-03/-02",
      :from 1087084800000},
     :history
     [{:offset -15652000,
       :rule "-",
       :format "LMT",
       :until -2372112000000}
      {:offset -15408000,
       :rule "-",
       :format "CMT",
       :until -1567468800000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until -1233446400000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until -7603200000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 667958400000}
      {:offset -14400000, :rule "-", :format "-04", :until 687916800000}
      {:offset -10800000,
       :rule "Arg",
       :format "-03/-02",
       :until 938908800000}
      {:offset -14400000,
       :rule "Arg",
       :format "-04/-03",
       :until 952041600000}
      {:offset -10800000, :rule "-", :format "-03", :until 1086048000000}
      {:offset -14400000,
       :rule "-",
       :format "-04",
       :until 1087084800000}]},
    "America/Marigot" "America/Port_of_Spain",
    "Pacific/Marquesas"
    {:current
     {:offset -34200000,
      :rule "-",
      :format "-0930",
      :from -1806710400000},
     :history
     [{:offset -33480000,
       :rule "-",
       :format "LMT",
       :until -1806710400000}]},
    "Asia/Baghdad"
    {:current
     {:offset 10800000,
      :rule "Iraq",
      :format "+03/+04",
      :from 389059200000},
     :history
     [{:offset 10660000, :rule "-", :format "LMT", :until -2524521600000}
      {:offset 10656000, :rule "-", :format "BMT", :until -1640995200000}
      {:offset 10800000,
       :rule "-",
       :format "+03",
       :until 389059200000}]},
    "America/Dawson_Creek"
    {:current
     {:offset -25200000, :rule "-", :format "MST", :from 83988000000},
     :history
     [{:offset -28856000,
       :rule "-",
       :format "LMT",
       :until -2713910400000}
      {:offset -28800000,
       :rule "Canada",
       :format "P%sT",
       :until -725846400000}
      {:offset -28800000,
       :rule "Vanc",
       :format "P%sT",
       :until 83988000000}]},
    "Europe/Kirov"
    {:current
     {:offset 10800000, :rule "-", :format "+03", :from 1414303200000},
     :history
     [{:offset 11928000, :rule "-", :format "LMT", :until -1593820800000}
      {:offset 10800000, :rule "-", :format "+03", :until -1247529600000}
      {:offset 14400000,
       :rule "Russia",
       :format "+04/+05",
       :until 606898800000}
      {:offset 10800000,
       :rule "Russia",
       :format "+03/+04",
       :until 670406400000}
      {:offset 14400000, :rule "-", :format "+04", :until 701852400000}
      {:offset 10800000,
       :rule "Russia",
       :format "+03/+04",
       :until 1301212800000}
      {:offset 14400000,
       :rule "-",
       :format "+04",
       :until 1414303200000}]},
    "America/Santo_Domingo"
    {:current
     {:offset -14400000, :rule "-", :format "AST", :from 975805200000},
     :history
     [{:offset -16776000,
       :rule "-",
       :format "LMT",
       :until -2524521600000}
      {:offset -16800000,
       :rule "-",
       :format "SDMT",
       :until -1159790400000}
      {:offset -18000000, :rule "DR", :format "%s", :until 152064000000}
      {:offset -14400000, :rule "-", :format "AST", :until 972784800000}
      {:offset -18000000,
       :rule "US",
       :format "E%sT",
       :until 975805200000}]},
    "America/Miquelon"
    {:current
     {:offset -10800000,
      :rule "Canada",
      :format "-03/-02",
      :from 536457600000},
     :history
     [{:offset -13480000,
       :rule "-",
       :format "LMT",
       :until -1850342400000}
      {:offset -14400000, :rule "-", :format "AST", :until 325987200000}
      {:offset -10800000,
       :rule "-",
       :format "-03",
       :until 536457600000}]},
    "Pacific/Wake"
    {:current
     {:offset 43200000, :rule "-", :format "+12", :from -2177452800000},
     :history
     [{:offset 39988000,
       :rule "-",
       :format "LMT",
       :until -2177452800000}]},
    "Asia/Ust-Nera"
    {:current
     {:offset 36000000, :rule "-", :format "+10", :from 1414335600000},
     :history
     [{:offset 34374000, :rule "-", :format "LMT", :until -1579392000000}
      {:offset 28800000, :rule "-", :format "+08", :until -1247529600000}
      {:offset 32400000,
       :rule "Russia",
       :format "+09/+10",
       :until 354931200000}
      {:offset 39600000,
       :rule "Russia",
       :format "+11/+12",
       :until 670424400000}
      {:offset 36000000,
       :rule "Russia",
       :format "+10/+11",
       :until 695829600000}
      {:offset 39600000,
       :rule "Russia",
       :format "+11/+12",
       :until 1301234400000}
      {:offset 43200000, :rule "-", :format "+12", :until 1315918800000}
      {:offset 39600000,
       :rule "-",
       :format "+11",
       :until 1414335600000}]},
    "Indian/Antananarivo" "Africa/Nairobi",
    "Europe/Malta"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 347155200000},
     :history
     [{:offset 3484000, :rule "-", :format "LMT", :until -2403475200000}
      {:offset 3600000,
       :rule "Italy",
       :format "CE%sT",
       :until 102384000000}
      {:offset 3600000,
       :rule "Malta",
       :format "CE%sT",
       :until 347155200000}]},
    "Europe/Paris"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 220924800000},
     :history
     [{:offset 561000, :rule "-", :format "LMT", :until -2486678400000}
      {:offset 561000, :rule "-", :format "PMT", :until -1855958340000}
      {:offset 0, :rule "France", :format "WE%sT", :until -932432400000}
      {:offset 3600000,
       :rule "C-Eur",
       :format "CE%sT",
       :until -800064000000}
      {:offset 0, :rule "France", :format "WE%sT", :until -766616400000}
      {:offset 3600000,
       :rule "France",
       :format "CE%sT",
       :until 220924800000}]},
    "Africa/Luanda" "Africa/Lagos",
    "Europe/Madrid"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 283996800000},
     :history
     [{:offset -884000, :rule "-", :format "LMT", :until -2177539200000}
      {:offset 0, :rule "Spain", :format "WE%sT", :until -940208400000}
      {:offset 3600000,
       :rule "Spain",
       :format "CE%sT",
       :until 283996800000}]},
    "Europe/Gibraltar"
    {:current
     {:offset 3600000, :rule "EU", :format "CE%sT", :from 378691200000},
     :history
     [{:offset -1284000, :rule "-", :format "LMT", :until -2821651200000}
      {:offset 0, :rule "GB-Eire", :format "%s", :until -401320800000}
      {:offset 3600000, :rule "-", :format "CET", :until 378691200000}]},
    "America/Cancun"
    {:current
     {:offset -18000000, :rule "-", :format "EST", :from 1422756000000},
     :history
     [{:offset -20824000,
       :rule "-",
       :format "LMT",
       :until -1514764800000}
      {:offset -21600000, :rule "-", :format "CST", :until 377913600000}
      {:offset -18000000,
       :rule "Mexico",
       :format "E%sT",
       :until 902023200000}
      {:offset -21600000,
       :rule "Mexico",
       :format "C%sT",
       :until 1422756000000}]},
    "Pacific/Port_Moresby"
    {:current
     {:offset 36000000, :rule "-", :format "+10", :from -2366755200000},
     :history
     [{:offset 35320000, :rule "-", :format "LMT", :until -2840140800000}
      {:offset 35312000,
       :rule "-",
       :format "PMMT",
       :until -2366755200000}]},
    "Atlantic/Cape_Verde"
    {:current
     {:offset -3600000, :rule "-", :format "-01", :from 186112800000},
     :history
     [{:offset -5644000, :rule "-", :format "LMT", :until -1830384000000}
      {:offset -7200000, :rule "-", :format "-02", :until -862617600000}
      {:offset -7200000,
       :rule 3600000,
       :format "-01",
       :until -764121600000}
      {:offset -7200000,
       :rule "-",
       :format "-02",
       :until 186112800000}]},
    "Australia/Darwin"
    {:current
     {:offset 34200000,
      :rule "Aus",
      :format "AC%sT",
      :from -2230156800000},
     :history
     [{:offset 31400000, :rule "-", :format "LMT", :until -2364076800000}
      {:offset 32400000,
       :rule "-",
       :format "ACST",
       :until -2230156800000}]},
    "Africa/Nairobi"
    {:current
     {:offset 10800000, :rule "-", :format "EAT", :from -315619200000},
     :history
     [{:offset 8836000, :rule "-", :format "LMT", :until -1309737600000}
      {:offset 10800000, :rule "-", :format "EAT", :until -1262304000000}
      {:offset 9000000, :rule "-", :format "+0230", :until -946771200000}
      {:offset 9900000,
       :rule "-",
       :format "+0245",
       :until -315619200000}]},
    "WET"
    {:current {:offset 0, :rule "EU", :format "WE%sT", :from nil},
     :history []},
    "Atlantic/Canary"
    {:current
     {:offset 0, :rule "EU", :format "WE%sT", :from 338954400000},
     :history
     [{:offset -3696000, :rule "-", :format "LMT", :until -1509667200000}
      {:offset -3600000, :rule "-", :format "-01", :until -733878000000}
      {:offset 0, :rule "-", :format "WET", :until 323827200000}
      {:offset 0, :rule 3600000, :format "WEST", :until 338954400000}]},
    "Africa/Libreville" "Africa/Lagos",
    "America/Paramaribo"
    {:current
     {:offset -10800000, :rule "-", :format "-03", :from 465436800000},
     :history
     [{:offset -13240000,
       :rule "-",
       :format "LMT",
       :until -1861920000000}
      {:offset -13252000,
       :rule "-",
       :format "PMT",
       :until -1104537600000}
      {:offset -13236000, :rule "-", :format "PMT", :until -765331200000}
      {:offset -12600000,
       :rule "-",
       :format "-0330",
       :until 465436800000}]},
    "Asia/Ho_Chi_Minh"
    {:current
     {:offset 25200000, :rule "-", :format "+07", :from 171849600000},
     :history
     [{:offset 25600000, :rule "-", :format "LMT", :until -2004048000000}
      {:offset 25590000,
       :rule "-",
       :format "PLMT",
       :until -1851552000000}
      {:offset 25200000, :rule "-", :format "+07", :until -852080400000}
      {:offset 28800000, :rule "-", :format "+08", :until -782614800000}
      {:offset 32400000, :rule "-", :format "+09", :until -767836800000}
      {:offset 25200000, :rule "-", :format "+07", :until -718070400000}
      {:offset 28800000, :rule "-", :format "+08", :until -457747200000}
      {:offset 25200000, :rule "-", :format "+07", :until -315622800000}
      {:offset 28800000,
       :rule "-",
       :format "+08",
       :until 171849600000}]},
    "Europe/Vatican" "Europe/Rome",
    "Africa/Ndjamena"
    {:current
     {:offset 3600000, :rule "-", :format "WAT", :from 321321600000},
     :history
     [{:offset 3612000, :rule "-", :format "LMT", :until -1830384000000}
      {:offset 3600000, :rule "-", :format "WAT", :until 308707200000}
      {:offset 3600000,
       :rule 3600000,
       :format "WAST",
       :until 321321600000}]},
    "Australia/Melbourne"
    {:current
     {:offset 36000000, :rule "AV", :format "AE%sT", :from 31536000000},
     :history
     [{:offset 34792000, :rule "-", :format "LMT", :until -2364076800000}
      {:offset 36000000,
       :rule "Aus",
       :format "AE%sT",
       :until 31536000000}]},
    "America/Manaus"
    {:current
     {:offset -14400000, :rule "-", :format "-04", :from 780192000000},
     :history
     [{:offset -14404000,
       :rule "-",
       :format "LMT",
       :until -1767225600000}
      {:offset -14400000,
       :rule "Brazil",
       :format "-04/-03",
       :until 590025600000}
      {:offset -14400000, :rule "-", :format "-04", :until 749174400000}
      {:offset -14400000,
       :rule "Brazil",
       :format "-04/-03",
       :until 780192000000}]},
    "America/Adak"
    {:current
     {:offset -36000000, :rule "US", :format "H%sT", :from 438998400000},
     :history
     [{:offset 44002000, :rule "-", :format "LMT", :until -3225225600000}
      {:offset -42398000,
       :rule "-",
       :format "LMT",
       :until -2188987200000}
      {:offset -39600000, :rule "-", :format "NST", :until -883612800000}
      {:offset -39600000,
       :rule "US",
       :format "N%sT",
       :until -757382400000}
      {:offset -39600000, :rule "-", :format "NST", :until -86918400000}
      {:offset -39600000, :rule "-", :format "BST", :until -31536000000}
      {:offset -39600000,
       :rule "US",
       :format "B%sT",
       :until 436327200000}
      {:offset -36000000,
       :rule "US",
       :format "AH%sT",
       :until 438998400000}]},
    "Asia/Taipei"
    {:current
     {:offset 28800000,
      :rule "Taiwan",
      :format "C%sT",
      :from -766191600000},
     :history
     [{:offset 29160000, :rule "-", :format "LMT", :until -2335219200000}
      {:offset 28800000, :rule "-", :format "CST", :until -1017792000000}
      {:offset 32400000,
       :rule "-",
       :format "JST",
       :until -766191600000}]},
    "Asia/Dhaka"
    {:current
     {:offset 21600000,
      :rule "Dhaka",
      :format "+06/+07",
      :from 1230768000000},
     :history
     [{:offset 21700000, :rule "-", :format "LMT", :until -2524521600000}
      {:offset 21200000, :rule "-", :format "HMT", :until -891561600000}
      {:offset 23400000,
       :rule "-",
       :format "+0630",
       :until -872035200000}
      {:offset 19800000,
       :rule "-",
       :format "+0530",
       :until -862617600000}
      {:offset 23400000,
       :rule "-",
       :format "+0630",
       :until -576115200000}
      {:offset 21600000,
       :rule "-",
       :format "+06",
       :until 1230768000000}]}},
   :rules
   {"CR"
    {:current nil,
     :history
     [[[{:utc 288748800000, :clock? :utc}
        {:utc 297043200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 3600000}]
      [[{:utc 297043200000, :clock? :utc}
        {:utc 663897600000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 6,
        :save 0}]
      [[{:utc 678326400000, :clock? :utc}
        {:utc 663897600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 7, :save 0}]
      [[{:utc 663897600000, :clock? :utc}
        {:utc 700617600000, :clock? :utc}]
       {:floating-day "Sat>=15",
        :time {:hour 0, :minute 0},
        :month 1,
        :save 3600000}]]},
    "Pakistan"
    {:current nil,
     :history
     [[[{:utc 1017705600000, :clock? :utc}
        {:utc 1033516800000, :clock? :utc}]
       {:floating-day "Sun>=2",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 1033516800000, :clock? :utc}
        {:utc 1212278400000, :clock? :utc}]
       {:floating-day "Sun>=2",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 1212278400000, :clock? :utc}
        {:utc 1225497600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 1225497600000, :clock? :utc}
        {:utc 1239753600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 0}]]},
    "Nic"
    {:current nil,
     :history
     [[[{:utc 290390400000, :clock? :utc}
        {:utc 298944000000, :clock? :utc}]
       {:floating-day "Sun>=16",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 298944000000, :clock? :utc}
        {:utc 1113091200000, :clock? :utc}]
       {:floating-day "Mon>=23",
        :time {:hour 0, :minute 0},
        :month 6,
        :save 0}]
      [[{:utc 1113091200000, :clock? :utc}
        {:utc 1128124800000, :clock? :utc}]
       {:day 10, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 1128124800000, :clock? :utc}
        {:utc 1146362400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 1146362400000, :clock? :utc}
        {:utc 1159660800000, :clock? :utc}]
       {:day 30, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]]},
    "Perry"
    {:current nil,
     :history
     [[[{:utc -747273600000, :clock? :utc}
        {:utc -733968000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -733968000000, :clock? :utc}
        {:utc -526521600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -526521600000, :clock? :utc}
        {:utc -513216000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -463017600000, :clock? :utc}
        {:utc -513216000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -513216000000, :clock? :utc}
        {:utc -431568000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -289440000000, :clock? :utc}
        {:utc -431568000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -261014400000, :clock? :utc}
        {:utc -431568000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -431568000000, :clock? :utc}
        {:utc -226540800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Tunisia"
    {:current nil,
     :history
     [[[{:utc -969238800000, :clock? :standard}
        {:utc -950490000000, :clock? :standard}]
       {:day 15,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -950490000000, :clock? :standard}
        {:utc -941936400000, :clock? :standard}]
       {:day 18,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 11,
        :save 0}]
      [[{:utc -941936400000, :clock? :standard}
        {:utc -891129600000, :clock? :utc}]
       {:day 25,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 2,
        :save 3600000}]
      [[{:utc -891129600000, :clock? :utc}
        {:utc -877824000000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -877824000000, :clock? :utc}
        {:utc -857250000000, :clock? :utc}]
       {:day 9, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -857250000000, :clock? :utc}
        {:utc -844556400000, :clock? :utc}]
       {:day 2, :time {:hour 3, :minute 0}, :month 11, :save 0}]
      [[{:utc -844556400000, :clock? :utc}
        {:utc -842911200000, :clock? :utc}]
       {:day 29, :time {:hour 2, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -842911200000, :clock? :utc}
        {:utc -842220000000, :clock? :utc}]
       {:day 17, :time {:hour 2, :minute 0}, :month 4, :save 0}]
      [[{:utc -842220000000, :clock? :utc}
        {:utc -828223200000, :clock? :utc}]
       {:day 25, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -828223200000, :clock? :utc}
        {:utc -812678400000, :clock? :utc}]
       {:day 4, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -796262400000, :clock? :utc}
        {:utc -812678400000, :clock? :utc}]
       {:day 8, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -812678400000, :clock? :utc}
        {:utc -766627200000, :clock? :utc}]
       {:floating-day "Mon>=1",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -766627200000, :clock? :utc}
        {:utc 231206400000, :clock? :standard}]
       {:day 16, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 231206400000, :clock? :standard}
        {:utc 243907200000, :clock? :standard}]
       {:day 30,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 243907200000, :clock? :standard}
        {:utc 262828800000, :clock? :standard}]
       {:day 24,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 262828800000, :clock? :standard}
        {:utc 276048000000, :clock? :standard}]
       {:day 1,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc 276048000000, :clock? :standard}
        {:utc 581126400000, :clock? :standard}]
       {:day 1,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 581126400000, :clock? :standard}
        {:utc 591148800000, :clock? :standard}]
       {:day 1,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 6,
        :save 3600000}]
      [[{:utc 606873600000, :clock? :standard}
        {:utc 591148800000, :clock? :standard}]
       {:day 26,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 591148800000, :clock? :standard}
        {:utc 641520000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 641520000000, :clock? :standard}
        {:utc 1128042000000, :clock? :standard}]
       {:day 1,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc 1114905600000, :clock? :standard}
        {:utc 1128042000000, :clock? :standard}]
       {:day 1,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc 1128042000000, :clock? :standard}
        {:utc 1143331200000, :clock? :standard}]
       {:day 30,
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 1143331200000, :clock? :standard}
        {:utc 1162080000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]]},
    "Uruguay"
    {:current nil,
     :history
     [[[{:utc -1459641600000, :clock? :utc}
        {:utc -1443830400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 1800000}]
      [[{:utc -1443830400000, :clock? :utc}
        {:utc -1141603200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 0}]
      [[{:utc -1141603200000, :clock? :utc}
        {:utc -1128384000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 1800000}]
      [[{:utc -954720000000, :clock? :utc}
        {:utc -1128384000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 1800000}]
      [[{:utc -920851200000, :clock? :utc}
        {:utc -1128384000000, :clock? :utc}]
       {:day 27, :time {:hour 0, :minute 0}, :month 10, :save 1800000}]
      [[{:utc -1128384000000, :clock? :utc}
        {:utc -896832000000, :clock? :utc}]
       {:floating-day "lastSat",
        :time {:hour 24, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc -896832000000, :clock? :utc}
        {:utc -845856000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 8, :save 1800000}]
      [[{:utc -853632000000, :clock? :utc}
        {:utc -845856000000, :clock? :utc}]
       {:day 14, :time {:hour 0, :minute 0}, :month 12, :save 1800000}]
      [[{:utc -845856000000, :clock? :utc}
        {:utc -334800000000, :clock? :utc}]
       {:day 14, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc -334800000000, :clock? :utc}
        {:utc -319680000000, :clock? :utc}]
       {:day 24, :time {:hour 0, :minute 0}, :month 5, :save 1800000}]
      [[{:utc -319680000000, :clock? :utc}
        {:utc -314236800000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc -314236800000, :clock? :utc}
        {:utc -310003200000, :clock? :utc}]
       {:day 17, :time {:hour 0, :minute 0}, :month 1, :save 3600000}]
      [[{:utc -310003200000, :clock? :utc}
        {:utc -149731200000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc -149731200000, :clock? :utc}
        {:utc -134611200000, :clock? :utc}]
       {:day 4, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -134611200000, :clock? :utc}
        {:utc -50457600000, :clock? :utc}]
       {:day 26, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -50457600000, :clock? :utc}
        {:utc -34214400000, :clock? :utc}]
       {:day 27, :time {:hour 0, :minute 0}, :month 5, :save 1800000}]
      [[{:utc -34214400000, :clock? :utc}
        {:utc 9849600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 12, :save 0}]
      [[{:utc 9849600000, :clock? :utc} {:utc 14169600000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 14169600000, :clock? :utc}
        {:utc 72835200000, :clock? :utc}]
       {:day 14, :time {:hour 0, :minute 0}, :month 6, :save 0}]
      [[{:utc 72835200000, :clock? :utc}
        {:utc 80092800000, :clock? :utc}]
       {:day 23, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 80092800000, :clock? :utc}
        {:utc 127267200000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 7, :save 0}]
      [[{:utc 127267200000, :clock? :utc}
        {:utc 147225600000, :clock? :utc}]
       {:day 13, :time {:hour 0, :minute 0}, :month 1, :save 5400000}]
      [[{:utc 132105600000, :clock? :utc}
        {:utc 147225600000, :clock? :utc}]
       {:day 10, :time {:hour 0, :minute 0}, :month 3, :save 1800000}]
      [[{:utc 147225600000, :clock? :utc}
        {:utc 156902400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 156902400000, :clock? :utc}
        {:utc 165369600000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc 165369600000, :clock? :utc}
        {:utc 219801600000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc 219801600000, :clock? :utc}
        {:utc 226454400000, :clock? :utc}]
       {:day 19, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc 226454400000, :clock? :utc}
        {:utc 250041600000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc 250041600000, :clock? :utc}
        {:utc 257558400000, :clock? :utc}]
       {:day 4, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc 282700800000, :clock? :utc}
        {:utc 257558400000, :clock? :utc}]
       {:day 17, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc 257558400000, :clock? :utc}
        {:utc 294192000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc 294192000000, :clock? :utc}
        {:utc 322012800000, :clock? :utc}]
       {:day 29, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 322012800000, :clock? :utc}
        {:utc 566438400000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc 566438400000, :clock? :utc}
        {:utc 573004800000, :clock? :utc}]
       {:day 14, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc 573004800000, :clock? :utc}
        {:utc 597801600000, :clock? :utc}]
       {:day 28, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 597801600000, :clock? :utc}
        {:utc 605059200000, :clock? :utc}]
       {:day 11, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc 605059200000, :clock? :utc}
        {:utc 625622400000, :clock? :utc}]
       {:day 5, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc 625622400000, :clock? :utc}
        {:utc 635904000000, :clock? :utc}]
       {:day 29, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 635904000000, :clock? :utc}
        {:utc 656467200000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 656467200000, :clock? :utc}
        {:utc 667785600000, :clock? :utc}]
       {:floating-day "Sun>=21",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 3600000}]
      [[{:utc 667785600000, :clock? :utc}
        {:utc 719366400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc 719366400000, :clock? :utc}
        {:utc 730857600000, :clock? :utc}]
       {:day 18, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 730857600000, :clock? :utc}
        {:utc 1095552000000, :clock? :utc}]
       {:day 28, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 1095552000000, :clock? :utc}
        {:utc 1111885200000, :clock? :utc}]
       {:day 19, :time {:hour 0, :minute 0}, :month 9, :save 3600000}]
      [[{:utc 1111885200000, :clock? :utc}
        {:utc 1128823200000, :clock? :utc}]
       {:day 27, :time {:hour 2, :minute 0}, :month 3, :save 0}]
      [[{:utc 1128823200000, :clock? :utc}
        {:utc 1141776000000, :clock? :utc}]
       {:day 9, :time {:hour 2, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 1159660800000, :clock? :utc}
        {:utc 1141776000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 3600000}]]},
    "Denver"
    {:current nil,
     :history
     [[[{:utc -1551657600000, :clock? :utc}
        {:utc -1570406400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -1570406400000, :clock? :utc}
        {:utc -1534111200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc -1534111200000, :clock? :utc}
        {:utc -147916800000, :clock? :utc}]
       {:day 22, :time {:hour 2, :minute 0}, :month 5, :save 0}]
      [[{:utc -147916800000, :clock? :utc}
        {:utc -131587200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Hungary"
    {:current nil,
     :history
     [[[{:utc -1633208400000, :clock? :utc}
        {:utc -1618693200000, :clock? :utc}]
       {:day 1, :time {:hour 3, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1618693200000, :clock? :utc}
        {:utc -1600462800000, :clock? :utc}]
       {:day 16, :time {:hour 3, :minute 0}, :month 9, :save 0}]
      [[{:utc -1600462800000, :clock? :utc}
        {:utc -1581195600000, :clock? :utc}]
       {:day 15, :time {:hour 3, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1581195600000, :clock? :utc}
        {:utc -778467600000, :clock? :utc}]
       {:day 24, :time {:hour 3, :minute 0}, :month 11, :save 0}]
      [[{:utc -778467600000, :clock? :utc}
        {:utc -762652800000, :clock? :utc}]
       {:day 1, :time {:hour 23, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -762652800000, :clock? :utc}
        {:utc -749685600000, :clock? :standard}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc -749685600000, :clock? :standard}
        {:utc -733795200000, :clock? :standard}]
       {:day 31,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -733795200000, :clock? :standard}
        {:utc -717811200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -717811200000, :clock? :standard}
        {:utc -605656800000, :clock? :standard}]
       {:floating-day "Sun>=4",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -621986400000, :clock? :standard}
        {:utc -605656800000, :clock? :standard}]
       {:day 17,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -605656800000, :clock? :standard}
        {:utc -492652800000, :clock? :utc}]
       {:day 23,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -492652800000, :clock? :utc}
        {:utc -481161600000, :clock? :utc}]
       {:day 23, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -481161600000, :clock? :utc}
        {:utc -428716800000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -428716800000, :clock? :utc}
        {:utc -418262400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 6,
        :save 3600000}]
      [[{:utc -418262400000, :clock? :utc}
        {:utc -397180800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -397180800000, :clock? :utc}
        {:utc -386812800000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 1, :minute 0},
        :month 6,
        :save 3600000}]
      [[{:utc -386812800000, :clock? :utc}
        {:utc 323830800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 3, :minute 0},
        :month 9,
        :save 0}]]},
    "Cuba"
    {:current
     {:standard
      {:from 1351728000000,
       :clock? :standard,
       :floating-day "Sun>=1",
       :time {:hour 0, :minute 0, :time-suffix "s"},
       :month 11,
       :save 0},
      :daylight-savings
      {:from 1362700800000,
       :clock? :standard,
       :floating-day "Sun>=8",
       :time {:hour 0, :minute 0, :time-suffix "s"},
       :month 3,
       :save 3600000}},
     :history
     [[[{:utc -1311552000000, :clock? :utc}
        {:utc -1301011200000, :clock? :utc}]
       {:day 10, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -1301011200000, :clock? :utc}
        {:utc -933638400000, :clock? :utc}]
       {:day 10, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -933638400000, :clock? :utc}
        {:utc -925689600000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 6,
        :save 3600000}]
      [[{:utc -925689600000, :clock? :utc}
        {:utc -775872000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -775872000000, :clock? :utc}
        {:utc -767923200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 6,
        :save 3600000}]
      [[{:utc -767923200000, :clock? :utc}
        {:utc -144720000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -144720000000, :clock? :utc}
        {:utc -134265600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -134265600000, :clock? :utc}
        {:utc -113443200000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -113443200000, :clock? :utc}
        {:utc -102556800000, :clock? :utc}]
       {:day 29, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -102556800000, :clock? :utc}
        {:utc -86313600000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -86313600000, :clock? :utc}
        {:utc -73094400000, :clock? :utc}]
       {:day 8, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -73094400000, :clock? :utc}
        {:utc -54172800000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -54172800000, :clock? :utc}
        {:utc -5788800000, :clock? :utc}]
       {:day 14, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -5788800000, :clock? :utc}
        {:utc -21513600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 87350400000, :clock? :utc}
        {:utc -21513600000, :clock? :utc}]
       {:day 8, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -21513600000, :clock? :utc}
        {:utc 183513600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 183513600000, :clock? :utc}
        {:utc 263347200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 263347200000, :clock? :utc}
        {:utc 276652800000, :clock? :utc}]
       {:day 7, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 290304000000, :clock? :utc}
        {:utc 276652800000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 357868800000, :clock? :utc}
        {:utc 276652800000, :clock? :utc}]
       {:floating-day "Sun>=5",
        :time {:hour 0, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc 511142400000, :clock? :utc}
        {:utc 276652800000, :clock? :utc}]
       {:floating-day "Sun>=14",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 276652800000, :clock? :utc}
        {:utc 638928000000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 686880000000, :clock? :standard}
        {:utc 638928000000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 844560000000, :clock? :standard}
        {:utc 638928000000, :clock? :utc}]
       {:day 6,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 638928000000, :clock? :utc}
        {:utc 876614400000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 876614400000, :clock? :standard}
        {:utc 891129600000, :clock? :standard}]
       {:day 12,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 891129600000, :clock? :standard}
        {:utc 909273600000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 909273600000, :clock? :standard}
        {:utc 954547200000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 954547200000, :clock? :standard}
        {:utc 1162080000000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 1080432000000, :clock? :standard}
        {:utc 1162080000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 1173312000000, :clock? :standard}
        {:utc 1162080000000, :clock? :standard}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 1205539200000, :clock? :standard}
        {:utc 1162080000000, :clock? :standard}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 1162080000000, :clock? :standard}
        {:utc 1236470400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 1236470400000, :clock? :standard}
        {:utc 1321142400000, :clock? :standard}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 1300147200000, :clock? :standard}
        {:utc 1321142400000, :clock? :standard}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 1321142400000, :clock? :standard}
        {:utc 1333238400000, :clock? :standard}]
       {:day 13,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 11,
        :save 0}]
      [[{:utc 1333238400000, :clock? :standard}
        {:utc 1351728000000, :clock? :standard}]
       {:day 1,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]]},
    "E-Eur"
    {:current
     {:daylight-savings
      {:from 354672000000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 0, :minute 0},
       :month 3,
       :save 3600000},
      :standard
      {:from 846374400000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 0, :minute 0},
       :month 10,
       :save 0}},
     :history
     [[[{:utc 243993600000, :clock? :utc}
        {:utc 228700800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 276048000000, :clock? :utc}
        {:utc 228700800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 228700800000, :clock? :utc}
        {:utc 307497600000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 307497600000, :clock? :utc}
        {:utc 354672000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]]},
    "Haiti"
    {:current
     {:daylight-savings
      {:from 1488931200000,
       :clock? :utc,
       :floating-day "Sun>=8",
       :time {:hour 2, :minute 0},
       :month 3,
       :save 3600000},
      :standard
      {:from 1509494400000,
       :clock? :utc,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0},
       :month 11,
       :save 0}},
     :history
     [[[{:utc 421200000000, :clock? :utc}
        {:utc 436320000000, :clock? :utc}]
       {:day 8, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 452044800000, :clock? :utc}
        {:utc 436320000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 436320000000, :clock? :utc}
        {:utc 575856000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 575856000000, :clock? :standard}
        {:utc 594172800000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 594172800000, :clock? :standard}
        {:utc 1112313600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 1112313600000, :clock? :utc}
        {:utc 1130630400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 1130630400000, :clock? :utc}
        {:utc 1331164800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 1331164800000, :clock? :utc}
        {:utc 1351728000000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 1351728000000, :clock? :utc}
        {:utc 1488931200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 11,
        :save 0}]]},
    "Chicago"
    {:current nil,
     :history
     [[[{:utc -1563746400000, :clock? :utc}
        {:utc -1551657600000, :clock? :utc}]
       {:day 13, :time {:hour 2, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -1551657600000, :clock? :utc}
        {:utc -1538956800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -1538956800000, :clock? :utc}
        {:utc -1491782400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc -1491782400000, :clock? :utc}
        {:utc -1504483200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -1504483200000, :clock? :utc}
        {:utc -447292800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Shang"
    {:current nil,
     :history
     [[[{:utc -933465600000, :clock? :utc}
        {:utc -923097600000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -923097600000, :clock? :utc}
        {:utc -908755200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]]},
    "NT_YK"
    {:current nil,
     :history
     [[[{:utc -1632088800000, :clock? :utc}
        {:utc -1615154400000, :clock? :utc}]
       {:day 14, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1615154400000, :clock? :utc}
        {:utc -1597010400000, :clock? :utc}]
       {:day 27, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -1597010400000, :clock? :utc}
        {:utc -1583193600000, :clock? :utc}]
       {:day 25, :time {:hour 2, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1583193600000, :clock? :utc}
        {:utc -880236000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc -880236000000, :clock? :utc}
        {:utc -765410400000, :clock? :utc}]
       {:day 9, :time {:hour 2, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -769395600000, :clock? :utc}
        {:utc -765410400000, :clock? :utc}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "u"},
        :month 8,
        :save 3600000}]
      [[{:utc -765410400000, :clock? :utc}
        {:utc -147916800000, :clock? :utc}]
       {:day 30, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc -147916800000, :clock? :utc}
        {:utc -131587200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 7200000}]
      [[{:utc -131587200000, :clock? :utc}
        {:utc 325641600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 325641600000, :clock? :utc}
        {:utc 341366400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 341366400000, :clock? :utc}
        {:utc 544233600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]]},
    "Bulg"
    {:current nil,
     :history
     [[[{:utc 291769200000, :clock? :utc}
        {:utc 307587600000, :clock? :utc}]
       {:day 31, :time {:hour 23, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 307587600000, :clock? :utc}
        {:utc 323395200000, :clock? :utc}]
       {:day 1, :time {:hour 1, :minute 0}, :month 10, :save 0}]
      [[{:utc 339037200000, :clock? :utc}
        {:utc 323395200000, :clock? :utc}]
       {:day 29, :time {:hour 1, :minute 0}, :month 9, :save 0}]
      [[{:utc 370404000000, :clock? :utc}
        {:utc 323395200000, :clock? :utc}]
       {:day 27, :time {:hour 2, :minute 0}, :month 9, :save 0}]]},
    "Salv"
    {:current nil,
     :history
     [[[{:utc 546825600000, :clock? :utc}
        {:utc 559699200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 5,
        :save 3600000}]]},
    "Moldova"
    {:current
     {:daylight-savings
      {:from 859680000000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 2, :minute 0},
       :month 3,
       :save 3600000},
      :standard
      {:from 877824000000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 3, :minute 0},
       :month 10,
       :save 0}},
     :history []},
    "Winn"
    {:current nil,
     :history
     [[[{:utc -1694390400000, :clock? :utc}
        {:utc -1681689600000, :clock? :utc}]
       {:day 23, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1681689600000, :clock? :utc}
        {:utc -1632088800000, :clock? :utc}]
       {:day 17, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -1632088800000, :clock? :utc}
        {:utc -1615154400000, :clock? :utc}]
       {:day 14, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1615154400000, :clock? :utc}
        {:utc -1029708000000, :clock? :utc}]
       {:day 27, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -1029708000000, :clock? :utc}
        {:utc -1018216800000, :clock? :utc}]
       {:day 16, :time {:hour 2, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1018216800000, :clock? :utc}
        {:utc -880236000000, :clock? :utc}]
       {:day 26, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc -880236000000, :clock? :utc}
        {:utc -765417600000, :clock? :utc}]
       {:day 9, :time {:hour 2, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -769395600000, :clock? :utc}
        {:utc -765417600000, :clock? :utc}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "u"},
        :month 8,
        :save 3600000}]
      [[{:utc -765417600000, :clock? :utc}
        {:utc -746056800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -746056800000, :clock? :utc}
        {:utc -732751200000, :clock? :utc}]
       {:day 12, :time {:hour 2, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -732751200000, :clock? :utc}
        {:utc -715824000000, :clock? :utc}]
       {:day 13, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -715824000000, :clock? :utc}
        {:utc -702518400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -702518400000, :clock? :utc}
        {:utc -620776800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -620776800000, :clock? :utc}
        {:utc -607644000000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -607644000000, :clock? :utc}
        {:utc -589420800000, :clock? :utc}]
       {:day 30, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc -576115200000, :clock? :utc}
        {:utc -589420800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -321494400000, :clock? :utc}
        {:utc -589420800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -589420800000, :clock? :utc}
        {:utc -292464000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -292464000000, :clock? :utc}
        {:utc -210816000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -210816000000, :clock? :utc}
        {:utc -198108000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -198108000000, :clock? :utc}
        {:utc -116467200000, :clock? :standard}]
       {:day 22, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc -116467200000, :clock? :standard}
        {:utc -100137600000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -100137600000, :clock? :standard}
        {:utc 544233600000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]]},
    "Chile"
    {:current
     {:standard
      {:from 1462752000000,
       :clock? :utc,
       :floating-day "Sun>=9",
       :time {:hour 3, :minute 0, :time-suffix "u"},
       :month 5,
       :save 0},
      :daylight-savings
      {:from 1470700800000,
       :clock? :utc,
       :floating-day "Sun>=9",
       :time {:hour 4, :minute 0, :time-suffix "u"},
       :month 8,
       :save 3600000}},
     :history
     [[[{:utc -1336003200000, :clock? :utc}
        {:utc -1317600000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 9, :save 3600000}]
      [[{:utc -1317600000000, :clock? :utc}
        {:utc -36619200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 0}]
      [[{:utc -36619200000, :clock? :utc}
        {:utc -23922000000, :clock? :utc}]
       {:day 3,
        :time {:hour 4, :minute 0, :time-suffix "u"},
        :month 11,
        :save 3600000}]
      [[{:utc -23922000000, :clock? :utc}
        {:utc -3355200000, :clock? :utc}]
       {:day 30,
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 3,
        :save 0}]
      [[{:utc -3355200000, :clock? :utc} {:utc 7527600000, :clock? :utc}]
       {:day 23,
        :time {:hour 4, :minute 0, :time-suffix "u"},
        :month 11,
        :save 3600000}]
      [[{:utc 7527600000, :clock? :utc} {:utc 24278400000, :clock? :utc}]
       {:day 29,
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 3,
        :save 0}]
      [[{:utc 37767600000, :clock? :utc}
        {:utc 24278400000, :clock? :utc}]
       {:day 14,
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 3,
        :save 0}]
      [[{:utc 24278400000, :clock? :utc}
        {:utc 68947200000, :clock? :utc}]
       {:floating-day "Sun>=9",
        :time {:hour 4, :minute 0, :time-suffix "u"},
        :month 10,
        :save 3600000}]
      [[{:utc 118209600000, :clock? :utc}
        {:utc 68947200000, :clock? :utc}]
       {:day 30,
        :time {:hour 4, :minute 0, :time-suffix "u"},
        :month 9,
        :save 3600000}]
      [[{:utc 68947200000, :clock? :utc}
        {:utc 150508800000, :clock? :utc}]
       {:floating-day "Sun>=9",
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 3,
        :save 0}]
      [[{:utc 150508800000, :clock? :utc}
        {:utc 545194800000, :clock? :utc}]
       {:floating-day "Sun>=9",
        :time {:hour 4, :minute 0, :time-suffix "u"},
        :month 10,
        :save 3600000}]
      [[{:utc 545194800000, :clock? :utc}
        {:utc 592358400000, :clock? :utc}]
       {:day 12,
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 4,
        :save 0}]
      [[{:utc 592358400000, :clock? :utc}
        {:utc 573868800000, :clock? :utc}]
       {:floating-day "Sun>=9",
        :time {:hour 4, :minute 0, :time-suffix "u"},
        :month 10,
        :save 3600000}]
      [[{:utc 573868800000, :clock? :utc}
        {:utc 653457600000, :clock? :utc}]
       {:floating-day "Sun>=9",
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 3,
        :save 0}]
      [[{:utc 653457600000, :clock? :utc}
        {:utc 668476800000, :clock? :utc}]
       {:day 16,
        :time {:hour 4, :minute 0, :time-suffix "u"},
        :month 9,
        :save 3600000}]
      [[{:utc 668476800000, :clock? :utc}
        {:utc 686966400000, :clock? :utc}]
       {:floating-day "Sun>=9",
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 3,
        :save 0}]
      [[{:utc 686966400000, :clock? :utc}
        {:utc 859690800000, :clock? :utc}]
       {:floating-day "Sun>=9",
        :time {:hour 4, :minute 0, :time-suffix "u"},
        :month 10,
        :save 3600000}]
      [[{:utc 859690800000, :clock? :utc}
        {:utc 906868800000, :clock? :utc}]
       {:day 30,
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 3,
        :save 0}]
      [[{:utc 889401600000, :clock? :utc}
        {:utc 906868800000, :clock? :utc}]
       {:floating-day "Sun>=9",
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 3,
        :save 0}]
      [[{:utc 906868800000, :clock? :utc}
        {:utc 923194800000, :clock? :utc}]
       {:day 27,
        :time {:hour 4, :minute 0, :time-suffix "u"},
        :month 9,
        :save 3600000}]
      [[{:utc 923194800000, :clock? :utc}
        {:utc 939427200000, :clock? :utc}]
       {:day 4,
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 4,
        :save 0}]
      [[{:utc 952560000000, :clock? :utc}
        {:utc 939427200000, :clock? :utc}]
       {:floating-day "Sun>=9",
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 3,
        :save 0}]
      [[{:utc 1206846000000, :clock? :utc}
        {:utc 939427200000, :clock? :utc}]
       {:day 30,
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 3,
        :save 0}]
      [[{:utc 1236556800000, :clock? :utc}
        {:utc 939427200000, :clock? :utc}]
       {:floating-day "Sun>=9",
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 3,
        :save 0}]
      [[{:utc 939427200000, :clock? :utc}
        {:utc 1270080000000, :clock? :utc}]
       {:floating-day "Sun>=9",
        :time {:hour 4, :minute 0, :time-suffix "u"},
        :month 10,
        :save 3600000}]
      [[{:utc 1270080000000, :clock? :utc}
        {:utc 1313452800000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 4,
        :save 0}]
      [[{:utc 1304294400000, :clock? :utc}
        {:utc 1313452800000, :clock? :utc}]
       {:floating-day "Sun>=2",
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 5,
        :save 0}]
      [[{:utc 1313452800000, :clock? :utc}
        {:utc 1335139200000, :clock? :utc}]
       {:floating-day "Sun>=16",
        :time {:hour 4, :minute 0, :time-suffix "u"},
        :month 8,
        :save 3600000}]
      [[{:utc 1335139200000, :clock? :utc}
        {:utc 1346544000000, :clock? :utc}]
       {:floating-day "Sun>=23",
        :time {:hour 3, :minute 0, :time-suffix "u"},
        :month 4,
        :save 0}]
      [[{:utc 1346544000000, :clock? :utc}
        {:utc 1462752000000, :clock? :utc}]
       {:floating-day "Sun>=2",
        :time {:hour 4, :minute 0, :time-suffix "u"},
        :month 9,
        :save 3600000}]]},
    "SA"
    {:current nil,
     :history
     [[[{:utc -861408000000, :clock? :utc}
        {:utc -845769600000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 3600000}]]},
    "Aus"
    {:current nil,
     :history
     [[[{:utc -1672531140000, :clock? :utc}
        {:utc -1665352800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 1}, :month 1, :save 3600000}]
      [[{:utc -1665352800000, :clock? :utc}
        {:utc -883605600000, :clock? :utc}]
       {:day 25, :time {:hour 2, :minute 0}, :month 3, :save 0}]
      [[{:utc -883605600000, :clock? :utc}
        {:utc -876088800000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 1, :save 3600000}]
      [[{:utc -876088800000, :clock? :utc}
        {:utc -860364000000, :clock? :utc}]
       {:day 29, :time {:hour 2, :minute 0}, :month 3, :save 0}]
      [[{:utc -860364000000, :clock? :utc}
        {:utc -844646400000, :clock? :utc}]
       {:day 27, :time {:hour 2, :minute 0}, :month 9, :save 3600000}]
      [[{:utc -828309600000, :clock? :utc}
        {:utc -844646400000, :clock? :utc}]
       {:day 3, :time {:hour 2, :minute 0}, :month 10, :save 3600000}]]},
    "Brazil"
    {:current
     {:daylight-savings
      {:from 1541030400000,
       :clock? :utc,
       :floating-day "Sun>=1",
       :time {:hour 0, :minute 0},
       :month 11,
       :save 3600000},
      :standard
      {:from 2149804800000,
       :clock? :utc,
       :floating-day "Sun>=15",
       :time {:hour 0, :minute 0},
       :month 2,
       :save 0}},
     :history
     [[[{:utc -1206968400000, :clock? :utc}
        {:utc -1191369600000, :clock? :utc}]
       {:day 3, :time {:hour 11, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -1175385600000, :clock? :utc}
        {:utc -1191369600000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -1191369600000, :clock? :utc}
        {:utc -633830400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 0}]
      [[{:utc -622076400000, :clock? :utc}
        {:utc -633830400000, :clock? :utc}]
       {:day 16, :time {:hour 1, :minute 0}, :month 4, :save 0}]
      [[{:utc -633830400000, :clock? :utc}
        {:utc -591840000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc -591840000000, :clock? :utc}
        {:utc -191376000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 0}]
      [[{:utc -531360000000, :clock? :utc}
        {:utc -191376000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc -191376000000, :clock? :utc}
        {:utc -184204800000, :clock? :utc}]
       {:day 9, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc -184204800000, :clock? :utc}
        {:utc -155174400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc -155174400000, :clock? :utc}
        {:utc -150076800000, :clock? :utc}]
       {:day 31, :time {:hour 0, :minute 0}, :month 1, :save 3600000}]
      [[{:utc -150076800000, :clock? :utc}
        {:utc -128908800000, :clock? :utc}]
       {:day 31, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc -128908800000, :clock? :utc}
        {:utc -121132800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc -99964800000, :clock? :utc}
        {:utc -121132800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 3600000}]
      [[{:utc -121132800000, :clock? :utc}
        {:utc 499737600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc 499737600000, :clock? :utc}
        {:utc 511228800000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 11, :save 3600000}]
      [[{:utc 511228800000, :clock? :utc}
        {:utc 530582400000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc 530582400000, :clock? :utc}
        {:utc 540259200000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 540259200000, :clock? :utc}
        {:utc 562118400000, :clock? :utc}]
       {:day 14, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 562118400000, :clock? :utc}
        {:utc 571190400000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 571190400000, :clock? :utc}
        {:utc 592963200000, :clock? :utc}]
       {:day 7, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 592963200000, :clock? :utc}
        {:utc 602035200000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 602035200000, :clock? :utc}
        {:utc 624412800000, :clock? :utc}]
       {:day 29, :time {:hour 0, :minute 0}, :month 1, :save 0}]
      [[{:utc 624412800000, :clock? :utc}
        {:utc 634694400000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 634694400000, :clock? :utc}
        {:utc 656467200000, :clock? :utc}]
       {:day 11, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 656467200000, :clock? :utc}
        {:utc 666748800000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 666748800000, :clock? :utc}
        {:utc 687916800000, :clock? :utc}]
       {:day 17, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 687916800000, :clock? :utc}
        {:utc 697593600000, :clock? :utc}]
       {:day 20, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 697593600000, :clock? :utc}
        {:utc 719971200000, :clock? :utc}]
       {:day 9, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 719971200000, :clock? :utc}
        {:utc 728438400000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 728438400000, :clock? :utc}
        {:utc 750297600000, :clock? :utc}]
       {:day 31, :time {:hour 0, :minute 0}, :month 1, :save 0}]
      [[{:utc 750297600000, :clock? :utc}
        {:utc 761270400000, :clock? :utc}]
       {:floating-day "Sun>=11",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 3600000}]
      [[{:utc 761270400000, :clock? :utc}
        {:utc 844560000000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 823996800000, :clock? :utc}
        {:utc 844560000000, :clock? :utc}]
       {:day 11, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 844560000000, :clock? :utc}
        {:utc 856051200000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 856051200000, :clock? :utc}
        {:utc 876096000000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 876096000000, :clock? :utc}
        {:utc 888710400000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 888710400000, :clock? :utc}
        {:utc 908064000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc 908064000000, :clock? :utc}
        {:utc 919555200000, :clock? :utc}]
       {:day 11, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 919555200000, :clock? :utc}
        {:utc 938908800000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 938908800000, :clock? :utc}
        {:utc 951609600000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 951609600000, :clock? :utc}
        {:utc 970963200000, :clock? :utc}]
       {:day 27, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 970963200000, :clock? :utc}
        {:utc 982195200000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 3600000}]
      [[{:utc 1036281600000, :clock? :utc}
        {:utc 982195200000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 11, :save 3600000}]
      [[{:utc 1066521600000, :clock? :utc}
        {:utc 982195200000, :clock? :utc}]
       {:day 19, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 1099353600000, :clock? :utc}
        {:utc 982195200000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 11, :save 3600000}]
      [[{:utc 1129420800000, :clock? :utc}
        {:utc 982195200000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 982195200000, :clock? :utc}
        {:utc 1162684800000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 1162684800000, :clock? :utc}
        {:utc 1172361600000, :clock? :utc}]
       {:day 5, :time {:hour 0, :minute 0}, :month 11, :save 3600000}]
      [[{:utc 1172361600000, :clock? :utc}
        {:utc 1191801600000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 1191801600000, :clock? :utc}
        {:utc 1203033600000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 3600000}]
      [[{:utc 1203033600000, :clock? :utc}
        {:utc 1224028800000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 1329868800000, :clock? :utc}
        {:utc 1224028800000, :clock? :utc}]
       {:floating-day "Sun>=22",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 1360886400000, :clock? :utc}
        {:utc 1224028800000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 1424563200000, :clock? :utc}
        {:utc 1224028800000, :clock? :utc}]
       {:floating-day "Sun>=22",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 1224028800000, :clock? :utc}
        {:utc 1455494400000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 3600000}]
      [[{:utc 1455494400000, :clock? :utc}
        {:utc 1541030400000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 1677024000000, :clock? :utc}
        {:utc 1541030400000, :clock? :utc}]
       {:floating-day "Sun>=22",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 1707955200000, :clock? :utc}
        {:utc 1541030400000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 1771718400000, :clock? :utc}
        {:utc 1541030400000, :clock? :utc}]
       {:floating-day "Sun>=22",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 1802649600000, :clock? :utc}
        {:utc 1541030400000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 2024179200000, :clock? :utc}
        {:utc 1541030400000, :clock? :utc}]
       {:floating-day "Sun>=22",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 2055110400000, :clock? :utc}
        {:utc 1541030400000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 2118873600000, :clock? :utc}
        {:utc 1541030400000, :clock? :utc}]
       {:floating-day "Sun>=22",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]]},
    "EUAsia"
    {:current
     {:daylight-savings
      {:from 354672000000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 1, :minute 0, :time-suffix "u"},
       :month 3,
       :save 3600000},
      :standard
      {:from 846374400000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 1, :minute 0, :time-suffix "u"},
       :month 10,
       :save 0}},
     :history
     [[[{:utc 307497600000, :clock? :utc}
        {:utc 354672000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "u"},
        :month 9,
        :save 0}]]},
    "Hond"
    {:current nil,
     :history
     [[[{:utc 546825600000, :clock? :utc}
        {:utc 559699200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc 559699200000, :clock? :utc}
        {:utc 1146441600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 1146441600000, :clock? :utc}
        {:utc 1154390400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 5,
        :save 3600000}]]},
    "SpainAfrica"
    {:current nil,
     :history
     [[[{:utc -81432000000, :clock? :utc}
        {:utc -71107200000, :clock? :utc}]
       {:day 3, :time {:hour 12, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -71107200000, :clock? :utc}
        {:utc 141264000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 141264000000, :clock? :utc}
        {:utc 147225600000, :clock? :utc}]
       {:day 24, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 147225600000, :clock? :utc}
        {:utc 199756800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 207705600000, :clock? :utc}
        {:utc 199756800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 8, :save 0}]
      [[{:utc 199756800000, :clock? :utc}
        {:utc 244252800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 244252800000, :clock? :utc}
        {:utc 265507200000, :clock? :utc}]
       {:day 28, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 265507200000, :clock? :utc}
        {:utc 271036800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]]},
    "Sudan"
    {:current nil,
     :history
     [[[{:utc 10368000000, :clock? :utc}
        {:utc 24796800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 41817600000, :clock? :utc}
        {:utc 24796800000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 24796800000, :clock? :utc}
        {:utc 73440000000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 10, :save 0}]]},
    "EU"
    {:current
     {:daylight-savings
      {:from 354672000000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 1, :minute 0, :time-suffix "u"},
       :month 3,
       :save 3600000},
      :standard
      {:from 846374400000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 1, :minute 0, :time-suffix "u"},
       :month 10,
       :save 0}},
     :history
     [[[{:utc 243993600000, :clock? :utc}
        {:utc 228700800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "u"},
        :month 9,
        :save 0}]
      [[{:utc 276051600000, :clock? :utc}
        {:utc 228700800000, :clock? :utc}]
       {:day 1,
        :time {:hour 1, :minute 0, :time-suffix "u"},
        :month 10,
        :save 0}]
      [[{:utc 228700800000, :clock? :utc}
        {:utc 307497600000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 1, :minute 0, :time-suffix "u"},
        :month 4,
        :save 3600000}]
      [[{:utc 307497600000, :clock? :utc}
        {:utc 354672000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "u"},
        :month 9,
        :save 0}]]},
    "Pulaski"
    {:current nil,
     :history
     [[[{:utc -733968000000, :clock? :utc}
        {:utc -747273600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -447292800000, :clock? :utc}
        {:utc -747273600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -747273600000, :clock? :utc}
        {:utc -386812800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Arg"
    {:current nil,
     :history
     [[[{:utc -1233446400000, :clock? :utc}
        {:utc -1222992000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc -1222992000000, :clock? :utc}
        {:utc -1205971200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 0}]
      [[{:utc -1205971200000, :clock? :utc}
        {:utc -1194048000000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -1172880000000, :clock? :utc}
        {:utc -1194048000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 3600000}]
      [[{:utc -1194048000000, :clock? :utc}
        {:utc -931046400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc -931046400000, :clock? :utc}
        {:utc -900892800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 7, :save 3600000}]
      [[{:utc -900892800000, :clock? :utc}
        {:utc -890352000000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 6, :save 0}]
      [[{:utc -890352000000, :clock? :utc}
        {:utc -833760000000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -833760000000, :clock? :utc}
        {:utc -827280000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 8, :save 0}]
      [[{:utc -827280000000, :clock? :utc}
        {:utc -752284800000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -752284800000, :clock? :utc}
        {:utc -733795200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc -733795200000, :clock? :utc}
        {:utc -197337600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -197337600000, :clock? :utc}
        {:utc -190857600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -190857600000, :clock? :utc}
        {:utc -184204800000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc -184204800000, :clock? :utc}
        {:utc -164505600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc -164505600000, :clock? :utc}
        {:utc -86832000000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -86832000000, :clock? :utc}
        {:utc -71107200000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 4, :save 0}]
      [[{:utc -71107200000, :clock? :utc}
        {:utc -55296000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 3600000}]
      [[{:utc -55296000000, :clock? :utc}
        {:utc 128131200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 0}]
      [[{:utc 128131200000, :clock? :utc}
        {:utc 136598400000, :clock? :utc}]
       {:day 23, :time {:hour 0, :minute 0}, :month 1, :save 3600000}]
      [[{:utc 136598400000, :clock? :utc}
        {:utc 596937600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 0}]
      [[{:utc 596937600000, :clock? :utc}
        {:utc 604713600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc 624412800000, :clock? :utc}
        {:utc 604713600000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 3600000}]
      [[{:utc 604713600000, :clock? :utc}
        {:utc 938736000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc 938736000000, :clock? :utc}
        {:utc 952041600000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 3600000}]
      [[{:utc 952041600000, :clock? :utc}
        {:utc 1198972800000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc 1198972800000, :clock? :utc}
        {:utc 1205539200000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc 1224028800000, :clock? :utc}
        {:utc 1205539200000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 3600000}]]},
    "Palestine"
    {:current
     {:daylight-savings
      {:from 1458604800000,
       :clock? :utc,
       :floating-day "Sat>=22",
       :time {:hour 1, :minute 0},
       :month 3,
       :save 3600000},
      :standard
      {:from 1477699200000,
       :clock? :utc,
       :floating-day "lastSat",
       :time {:hour 1, :minute 0},
       :month 10,
       :save 0}},
     :history
     [[[{:utc 939945600000, :clock? :utc}
        {:utc 924134400000, :clock? :utc}]
       {:floating-day "Fri>=15",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 1096592400000, :clock? :utc}
        {:utc 924134400000, :clock? :utc}]
       {:day 1, :time {:hour 1, :minute 0}, :month 10, :save 0}]
      [[{:utc 924134400000, :clock? :utc}
        {:utc 1128391200000, :clock? :utc}]
       {:floating-day "Fri>=15",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 1128391200000, :clock? :utc}
        {:utc 1143849600000, :clock? :utc}]
       {:day 4, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc 1158883200000, :clock? :utc}
        {:utc 1143849600000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1143849600000, :clock? :utc}
        {:utc 1189209600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 1189209600000, :clock? :utc}
        {:utc 1206662400000, :clock? :utc}]
       {:floating-day "Thu>=8",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 1220227200000, :clock? :utc}
        {:utc 1206662400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1206662400000, :clock? :utc}
        {:utc 1251763200000, :clock? :utc}]
       {:floating-day "lastFri",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 1251763200000, :clock? :utc}
        {:utc 1269561600000, :clock? :utc}]
       {:floating-day "Fri>=1",
        :time {:hour 1, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 1269561600000, :clock? :utc}
        {:utc 1281484800000, :clock? :utc}]
       {:day 26, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1281484800000, :clock? :utc}
        {:utc 1301616060000, :clock? :utc}]
       {:day 11, :time {:hour 0, :minute 0}, :month 8, :save 0}]
      [[{:utc 1301616060000, :clock? :utc}
        {:utc 1312156800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 1}, :month 4, :save 3600000}]
      [[{:utc 1312156800000, :clock? :utc}
        {:utc 1314662400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 8, :save 0}]
      [[{:utc 1314662400000, :clock? :utc}
        {:utc 1317340800000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 8, :save 3600000}]
      [[{:utc 1317340800000, :clock? :utc}
        {:utc 1332979200000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1348189200000, :clock? :utc}
        {:utc 1332979200000, :clock? :utc}]
       {:day 21, :time {:hour 1, :minute 0}, :month 9, :save 0}]
      [[{:utc 1379721600000, :clock? :utc}
        {:utc 1332979200000, :clock? :utc}]
       {:floating-day "Fri>=21",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 1332979200000, :clock? :utc}
        {:utc 1413849600000, :clock? :utc}]
       {:floating-day "lastThu",
        :time {:hour 24, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 1413849600000, :clock? :utc}
        {:utc 1427414400000, :clock? :utc}]
       {:floating-day "Fri>=21",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 1427414400000, :clock? :utc}
        {:utc 1477699200000, :clock? :utc}]
       {:floating-day "lastFri",
        :time {:hour 24, :minute 0},
        :month 3,
        :save 3600000}]]},
    "Eire"
    {:current
     {:standard
      {:from 354672000000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 1, :minute 0, :time-suffix "u"},
       :month 3,
       :save 0},
      :daylight-savings
      {:from 846374400000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 1, :minute 0, :time-suffix "u"},
       :month 10,
       :save -3600000}},
     :history
     [[[{:utc 57722400000, :clock? :utc}
        {:utc 69552000000, :clock? :utc}]
       {:day 31,
        :time {:hour 2, :minute 0, :time-suffix "u"},
        :month 10,
        :save -3600000}]
      [[{:utc 69552000000, :clock? :utc}
        {:utc 88646400000, :clock? :utc}]
       {:floating-day "Sun>=16",
        :time {:hour 2, :minute 0, :time-suffix "u"},
        :month 3,
        :save 0}]
      [[{:utc 88646400000, :clock? :utc}
        {:utc 354672000000, :clock? :utc}]
       {:floating-day "Sun>=23",
        :time {:hour 2, :minute 0, :time-suffix "u"},
        :month 10,
        :save -3600000}]
      [[{:utc 372643200000, :clock? :utc}
        {:utc 354672000000, :clock? :utc}]
       {:floating-day "Sun>=23",
        :time {:hour 1, :minute 0, :time-suffix "u"},
        :month 10,
        :save -3600000}]
      [[{:utc 656553600000, :clock? :utc}
        {:utc 354672000000, :clock? :utc}]
       {:floating-day "Sun>=22",
        :time {:hour 1, :minute 0, :time-suffix "u"},
        :month 10,
        :save -3600000}]]},
    "Iraq"
    {:current nil,
     :history
     [[[{:utc 389059200000, :clock? :utc}
        {:utc 402278400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 417916800000, :clock? :utc}
        {:utc 402278400000, :clock? :utc}]
       {:day 31, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 402278400000, :clock? :utc}
        {:utc 449625600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 449625600000, :clock? :utc}
        {:utc 496800000000, :clock? :standard}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 496800000000, :clock? :standard}
        {:utc 512524800000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 512524800000, :clock? :standard}
        {:utc 686286000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 670474800000, :clock? :standard}
        {:utc 686286000000, :clock? :standard}]
       {:day 1,
        :time {:hour 3, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]]},
    "Canada"
    {:current
     {:daylight-savings
      {:from 1173312000000,
       :clock? :utc,
       :floating-day "Sun>=8",
       :time {:hour 2, :minute 0},
       :month 3,
       :save 3600000},
      :standard
      {:from 1193875200000,
       :clock? :utc,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0},
       :month 11,
       :save 0}},
     :history
     [[[{:utc -1632088800000, :clock? :utc}
        {:utc -1615154400000, :clock? :utc}]
       {:day 14, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1615154400000, :clock? :utc}
        {:utc -880236000000, :clock? :utc}]
       {:day 27, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -880236000000, :clock? :utc}
        {:utc -765410400000, :clock? :utc}]
       {:day 9, :time {:hour 2, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -769395600000, :clock? :utc}
        {:utc -765410400000, :clock? :utc}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "u"},
        :month 8,
        :save 3600000}]
      [[{:utc -765410400000, :clock? :utc}
        {:utc 136339200000, :clock? :utc}]
       {:day 30, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc 136339200000, :clock? :utc}
        {:utc 152064000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 152064000000, :clock? :utc}
        {:utc 544233600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 544233600000, :clock? :utc}
        {:utc 1193875200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Belgium"
    {:current nil,
     :history
     [[[{:utc -1635206400000, :clock? :standard}
        {:utc -1617408000000, :clock? :standard}]
       {:day 9,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1617408000000, :clock? :standard}
        {:utc -1604278800000, :clock? :standard}]
       {:floating-day "Sat>=1",
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1604278800000, :clock? :standard}
        {:utc -1552266000000, :clock? :standard}]
       {:day 1,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1574038800000, :clock? :standard}
        {:utc -1552266000000, :clock? :standard}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 2,
        :save 3600000}]
      [[{:utc -1552266000000, :clock? :standard}
        {:utc -1539997200000, :clock? :standard}]
       {:day 23,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1539997200000, :clock? :standard}
        {:utc -1520557200000, :clock? :standard}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1520557200000, :clock? :standard}
        {:utc -1507510800000, :clock? :standard}]
       {:day 25,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1507510800000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 25,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1473642000000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 21,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1444006800000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 29,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1411952400000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 4,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1379293200000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 17,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1491177600000, :clock? :standard}
        {:utc -1348448400000, :clock? :standard}]
       {:floating-day "Sat>=1",
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1348448400000, :clock? :standard}
        {:utc -1301702400000, :clock? :standard}]
       {:day 9,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1316394000000, :clock? :standard}
        {:utc -1301702400000, :clock? :standard}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1284328800000, :clock? :standard}
        {:utc -1301702400000, :clock? :standard}]
       {:day 21,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1253484000000, :clock? :standard}
        {:utc -1301702400000, :clock? :standard}]
       {:day 13,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1221429600000, :clock? :standard}
        {:utc -1301702400000, :clock? :standard}]
       {:day 19,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1191189600000, :clock? :standard}
        {:utc -1301702400000, :clock? :standard}]
       {:day 3,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1160344800000, :clock? :standard}
        {:utc -1301702400000, :clock? :standard}]
       {:day 26,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1127685600000, :clock? :standard}
        {:utc -1301702400000, :clock? :standard}]
       {:day 8,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1096840800000, :clock? :standard}
        {:utc -1301702400000, :clock? :standard}]
       {:day 31,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1063576800000, :clock? :standard}
        {:utc -1301702400000, :clock? :standard}]
       {:day 19,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1033336800000, :clock? :standard}
        {:utc -1301702400000, :clock? :standard}]
       {:day 4,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1301702400000, :clock? :standard}
        {:utc -1002492000000, :clock? :standard}]
       {:floating-day "Sun>=2",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1002492000000, :clock? :standard}
        {:utc -950479200000, :clock? :standard}]
       {:day 27,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -969228000000, :clock? :standard}
        {:utc -950479200000, :clock? :standard}]
       {:day 16,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -950479200000, :clock? :standard}
        {:utc -942012000000, :clock? :standard}]
       {:day 19,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 11,
        :save 0}]
      [[{:utc -942012000000, :clock? :standard}
        {:utc -798069600000, :clock? :standard}]
       {:day 25,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 2,
        :save 3600000}]
      [[{:utc -798069600000, :clock? :standard}
        {:utc -781048800000, :clock? :standard}]
       {:day 17,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -781048800000, :clock? :standard}
        {:utc -766620000000, :clock? :standard}]
       {:day 2,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -766620000000, :clock? :standard}
        {:utc -745452000000, :clock? :standard}]
       {:day 16,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -745452000000, :clock? :standard}
        {:utc -733269600000, :clock? :standard}]
       {:day 19,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]]},
    "Louisville"
    {:current nil,
     :history
     [[[{:utc -1535925600000, :clock? :utc}
        {:utc -1525298400000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1525298400000, :clock? :utc}
        {:utc -905126400000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc -891820800000, :clock? :utc}
        {:utc -905126400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -744242400000, :clock? :utc}
        {:utc -905126400000, :clock? :utc}]
       {:day 2, :time {:hour 2, :minute 0}, :month 6, :save 0}]
      [[{:utc -608169600000, :clock? :utc}
        {:utc -905126400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -415843200000, :clock? :utc}
        {:utc -905126400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]]},
    "Mexico"
    {:current
     {:daylight-savings
      {:from 1017619200000,
       :clock? :utc,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0},
       :month 4,
       :save 3600000},
      :standard
      {:from 1035676800000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 2, :minute 0},
       :month 10,
       :save 0}},
     :history
     [[[{:utc -975283200000, :clock? :utc}
        {:utc -963187200000, :clock? :utc}]
       {:day 5, :time {:hour 0, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -963187200000, :clock? :utc}
        {:utc -917136000000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 6, :save 0}]
      [[{:utc -917136000000, :clock? :utc}
        {:utc -907372800000, :clock? :utc}]
       {:day 9, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc -907372800000, :clock? :utc}
        {:utc -821923200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 0}]
      [[{:utc -821923200000, :clock? :utc}
        {:utc -810086400000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc -810086400000, :clock? :utc}
        {:utc -627523200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 0}]
      [[{:utc -627523200000, :clock? :utc}
        {:utc -613008000000, :clock? :utc}]
       {:day 12, :time {:hour 0, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -613008000000, :clock? :utc}
        {:utc 828316800000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 7, :save 0}]
      [[{:utc 828316800000, :clock? :utc}
        {:utc 846374400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 846374400000, :clock? :utc}
        {:utc 988675200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 988675200000, :clock? :utc}
        {:utc 1001808000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc 1001808000000, :clock? :utc}
        {:utc 1017619200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]]},
    "Libya"
    {:current nil,
     :history
     [[[{:utc -574898400000, :clock? :utc}
        {:utc -568080000000, :clock? :utc}]
       {:day 14, :time {:hour 2, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -568080000000, :clock? :utc}
        {:utc -512172000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 1, :save 0}]
      [[{:utc -512172000000, :clock? :utc}
        {:utc -504921600000, :clock? :utc}]
       {:day 9, :time {:hour 2, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -504921600000, :clock? :utc}
        {:utc -449884800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 1, :save 0}]
      [[{:utc -449884800000, :clock? :utc}
        {:utc -441849600000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 9, :save 3600000}]
      [[{:utc -441849600000, :clock? :utc}
        {:utc 386467200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 1, :save 0}]
      [[{:utc 386467200000, :clock? :utc}
        {:utc 402278400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 402278400000, :clock? :utc}
        {:utc 481593600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 481593600000, :clock? :utc}
        {:utc 528681600000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 512956800000, :clock? :utc}
        {:utc 528681600000, :clock? :utc}]
       {:day 4, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 528681600000, :clock? :utc}
        {:utc 544233600000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 544233600000, :clock? :utc}
        {:utc 560044800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 560044800000, :clock? :utc}
        {:utc 860112000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 860112000000, :clock? :utc}
        {:utc 875923200000, :clock? :utc}]
       {:day 4, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 875923200000, :clock? :utc}
        {:utc 1364515200000, :clock? :utc}]
       {:day 4, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 1364515200000, :clock? :utc}
        {:utc 1382659200000, :clock? :utc}]
       {:floating-day "lastFri",
        :time {:hour 1, :minute 0},
        :month 3,
        :save 3600000}]]},
    "W-Eur"
    {:current
     {:daylight-savings
      {:from 354672000000,
       :clock? :standard,
       :floating-day "lastSun",
       :time {:hour 1, :minute 0, :time-suffix "s"},
       :month 3,
       :save 3600000},
      :standard
      {:from 846374400000,
       :clock? :standard,
       :floating-day "lastSun",
       :time {:hour 1, :minute 0, :time-suffix "s"},
       :month 10,
       :save 0}},
     :history
     [[[{:utc 243993600000, :clock? :standard}
        {:utc 228700800000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 276051600000, :clock? :standard}
        {:utc 228700800000, :clock? :standard}]
       {:day 1,
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 228700800000, :clock? :standard}
        {:utc 307497600000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 307497600000, :clock? :standard}
        {:utc 354672000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]]},
    "Moncton"
    {:current nil,
     :history
     [[[{:utc -1153958400000, :clock? :utc}
        {:utc -1146009600000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 1, :minute 0},
        :month 6,
        :save 3600000}]
      [[{:utc -1146009600000, :clock? :utc}
        {:utc -1059868800000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 1, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -1059868800000, :clock? :utc}
        {:utc -1051920000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 1, :minute 0},
        :month 6,
        :save 3600000}]
      [[{:utc -1051920000000, :clock? :utc}
        {:utc -965689200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 1, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -965689200000, :clock? :utc}
        {:utc -955584000000, :clock? :utc}]
       {:day 27, :time {:hour 1, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -934758000000, :clock? :utc}
        {:utc -955584000000, :clock? :utc}]
       {:day 19, :time {:hour 1, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -955584000000, :clock? :utc}
        {:utc -904518000000, :clock? :utc}]
       {:floating-day "Sat>=21",
        :time {:hour 1, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -904518000000, :clock? :utc}
        {:utc -733968000000, :clock? :utc}]
       {:day 4, :time {:hour 1, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -733968000000, :clock? :utc}
        {:utc -747273600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -747273600000, :clock? :utc}
        {:utc -384393600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -384393600000, :clock? :utc}
        {:utc 733622400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 733622400000, :clock? :utc}
        {:utc 752025600000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 1},
        :month 4,
        :save 3600000}]]},
    "Detroit"
    {:current nil,
     :history
     [[[{:utc -684374400000, :clock? :utc}
        {:utc -671068800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Romania"
    {:current nil,
     :history
     [[[{:utc -1187049600000, :clock? :standard}
        {:utc -1175558400000, :clock? :standard}]
       {:day 21,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc -1175558400000, :clock? :standard}
        {:utc -1159747200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1159747200000, :clock? :standard}
        {:utc 307497600000, :clock? :utc}]
       {:floating-day "Sun>=2",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 296611200000, :clock? :utc}
        {:utc 307497600000, :clock? :utc}]
       {:day 27, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 307497600000, :clock? :utc}
        {:utc 323823600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 323823600000, :clock? :utc}
        {:utc 338947200000, :clock? :utc}]
       {:day 5, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 338947200000, :clock? :utc}
        {:utc 670377600000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 670377600000, :clock? :standard}
        {:utc 686102400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]]},
    "SanLuis"
    {:current nil,
     :history
     [[[{:utc 1191801600000, :clock? :utc}
        {:utc 1204934400000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 3600000}]]},
    "AT"
    {:current
     {:daylight-savings
      {:from 1001894400000,
       :clock? :standard,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0, :time-suffix "s"},
       :month 10,
       :save 3600000},
      :standard
      {:from 1207008000000,
       :clock? :standard,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0, :time-suffix "s"},
       :month 4,
       :save 0}},
     :history
     [[[{:utc -71107200000, :clock? :standard}
        {:utc -55382400000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc -55382400000, :clock? :standard}
        {:utc -37238400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc -25833600000, :clock? :standard}
        {:utc -37238400000, :clock? :standard}]
       {:floating-day "Sun>=8",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 67996800000, :clock? :standard}
        {:utc -37238400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 2,
        :save 0}]
      [[{:utc 99792000000, :clock? :standard}
        {:utc -37238400000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 386121600000, :clock? :standard}
        {:utc -37238400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc -37238400000, :clock? :standard}
        {:utc 446947200000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 446947200000, :clock? :standard}
        {:utc 529718400000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 529718400000, :clock? :standard}
        {:utc 542764800000, :clock? :standard}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 561859200000, :clock? :standard}
        {:utc 542764800000, :clock? :standard}]
       {:floating-day "Sun>=22",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 542764800000, :clock? :standard}
        {:utc 594172800000, :clock? :standard}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 594172800000, :clock? :standard}
        {:utc 670377600000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 686275200000, :clock? :standard}
        {:utc 670377600000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 967334400000, :clock? :standard}
        {:utc 670377600000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 8,
        :save 3600000}]
      [[{:utc 670377600000, :clock? :standard}
        {:utc 1001894400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 1143849600000, :clock? :standard}
        {:utc 1001894400000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 0}]
      [[{:utc 1174780800000, :clock? :standard}
        {:utc 1001894400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]]},
    "Malta"
    {:current nil,
     :history
     [[[{:utc 102384000000, :clock? :standard}
        {:utc 118108800000, :clock? :standard}]
       {:day 31,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 118108800000, :clock? :standard}
        {:utc 135734400000, :clock? :standard}]
       {:day 29,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 135734400000, :clock? :standard}
        {:utc 148521600000, :clock? :standard}]
       {:day 21,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 148521600000, :clock? :standard}
        {:utc 166752000000, :clock? :utc}]
       {:day 16,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 166752000000, :clock? :utc}
        {:utc 179971200000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 179971200000, :clock? :utc}
        {:utc 323316000000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]]},
    "Fiji"
    {:current
     {:daylight-savings
      {:from 1414800000000,
       :clock? :utc,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0},
       :month 11,
       :save 3600000},
      :standard
      {:from 1421193600000,
       :clock? :utc,
       :floating-day "Sun>=14",
       :time {:hour 3, :minute 0},
       :month 1,
       :save 0}},
     :history
     [[[{:utc 909878400000, :clock? :utc}
        {:utc 920160000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 11,
        :save 3600000}]
      [[{:utc 920160000000, :clock? :utc}
        {:utc 1259460000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 3, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 1259460000000, :clock? :utc}
        {:utc 1269734400000, :clock? :utc}]
       {:day 29, :time {:hour 2, :minute 0}, :month 11, :save 3600000}]
      [[{:utc 1269734400000, :clock? :utc}
        {:utc 1287619200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 3, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc 1298937600000, :clock? :utc}
        {:utc 1287619200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 3, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc 1287619200000, :clock? :utc}
        {:utc 1326844800000, :clock? :utc}]
       {:floating-day "Sun>=21",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 3600000}]
      [[{:utc 1326844800000, :clock? :utc}
        {:utc 1414800000000, :clock? :utc}]
       {:floating-day "Sun>=18",
        :time {:hour 3, :minute 0},
        :month 1,
        :save 0}]
      [[{:utc 1390003200000, :clock? :utc}
        {:utc 1414800000000, :clock? :utc}]
       {:floating-day "Sun>=18",
        :time {:hour 2, :minute 0},
        :month 1,
        :save 0}]]},
    "StJohns"
    {:current nil,
     :history
     [[[{:utc -1664143200000, :clock? :utc}
        {:utc -1650146400000, :clock? :utc}]
       {:day 8, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1650146400000, :clock? :utc}
        {:utc -1598662800000, :clock? :utc}]
       {:day 17, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc -1598662800000, :clock? :utc}
        {:utc -1590109200000, :clock? :utc}]
       {:day 5, :time {:hour 23, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1590109200000, :clock? :utc}
        {:utc -1567468800000, :clock? :utc}]
       {:day 12, :time {:hour 23, :minute 0}, :month 8, :save 0}]
      [[{:utc -1567468800000, :clock? :utc}
        {:utc -1551657600000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 23, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc -1551657600000, :clock? :utc}
        {:utc -1061856000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 23, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -1061856000000, :clock? :utc}
        {:utc -1049241600000, :clock? :utc}]
       {:floating-day "Mon>=9",
        :time {:hour 0, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc -1049241600000, :clock? :utc}
        {:utc -746409600000, :clock? :utc}]
       {:floating-day "Mon>=2",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -746409600000, :clock? :utc}
        {:utc -733708800000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 2, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc -733708800000, :clock? :utc}
        {:utc -589420800000, :clock? :utc}]
       {:floating-day "Sun>=2",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -576115200000, :clock? :utc}
        {:utc -589420800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -589420800000, :clock? :utc}
        {:utc -289440000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -289440000000, :clock? :utc}
        {:utc 544233600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 544233600000, :clock? :utc}
        {:utc 562118400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 1},
        :month 4,
        :save 3600000}]
      [[{:utc 575856000000, :clock? :utc}
        {:utc 562118400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 1},
        :month 4,
        :save 7200000}]
      [[{:utc 562118400000, :clock? :utc}
        {:utc 607392000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 1},
        :month 10,
        :save 0}]
      [[{:utc 607392000000, :clock? :utc}
        {:utc 1193875200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 1},
        :month 4,
        :save 3600000}]
      [[{:utc 1193875200000, :clock? :utc}
        {:utc 1173312000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 1},
        :month 11,
        :save 0}]]},
    "Egypt"
    {:current nil,
     :history
     [[[{:utc -929836800000, :clock? :utc}
        {:utc -923097600000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 7, :save 3600000}]
      [[{:utc -923097600000, :clock? :utc}
        {:utc -906163200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -906163200000, :clock? :utc}
        {:utc -892857600000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -892857600000, :clock? :utc}
        {:utc -875836800000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -857779200000, :clock? :utc}
        {:utc -875836800000, :clock? :utc}]
       {:day 27, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -875836800000, :clock? :utc}
        {:utc -825811200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -825811200000, :clock? :utc}
        {:utc -779846400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc -779846400000, :clock? :utc}
        {:utc -386640000000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -399081600000, :clock? :utc}
        {:utc -386640000000, :clock? :utc}]
       {:day 10, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -386640000000, :clock? :utc}
        {:utc -368323200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -368323200000, :clock? :utc}
        {:utc -323643600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -323643600000, :clock? :utc}
        {:utc -336783600000, :clock? :utc}]
       {:day 30, :time {:hour 3, :minute 0}, :month 9, :save 0}]
      [[{:utc -336783600000, :clock? :utc}
        {:utc -102632400000, :clock? :utc}]
       {:day 1, :time {:hour 1, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 396406800000, :clock? :utc}
        {:utc -102632400000, :clock? :utc}]
       {:day 25, :time {:hour 1, :minute 0}, :month 7, :save 3600000}]
      [[{:utc 426819600000, :clock? :utc}
        {:utc -102632400000, :clock? :utc}]
       {:day 12, :time {:hour 1, :minute 0}, :month 7, :save 3600000}]
      [[{:utc 452221200000, :clock? :utc}
        {:utc -102632400000, :clock? :utc}]
       {:day 1, :time {:hour 1, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 610419600000, :clock? :utc}
        {:utc -102632400000, :clock? :utc}]
       {:day 6, :time {:hour 1, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -102632400000, :clock? :utc}
        {:utc 641523600000, :clock? :utc}]
       {:day 1, :time {:hour 3, :minute 0}, :month 10, :save 0}]
      [[{:utc 641523600000, :clock? :utc}
        {:utc 812246400000, :clock? :utc}]
       {:day 1, :time {:hour 1, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 812246400000, :clock? :utc}
        {:utc 799027200000, :clock? :standard}]
       {:floating-day "lastThu",
        :time {:hour 24, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 1158883200000, :clock? :utc}
        {:utc 799027200000, :clock? :standard}]
       {:day 21, :time {:hour 24, :minute 0}, :month 9, :save 0}]
      [[{:utc 1188604800000, :clock? :utc}
        {:utc 799027200000, :clock? :standard}]
       {:floating-day "Thu>=1",
        :time {:hour 24, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 1219881600000, :clock? :utc}
        {:utc 799027200000, :clock? :standard}]
       {:floating-day "lastThu",
        :time {:hour 24, :minute 0},
        :month 8,
        :save 0}]
      [[{:utc 1250812800000, :clock? :utc}
        {:utc 799027200000, :clock? :standard}]
       {:day 20, :time {:hour 24, :minute 0}, :month 8, :save 0}]
      [[{:utc 799027200000, :clock? :standard}
        {:utc 1281484800000, :clock? :utc}]
       {:floating-day "lastFri",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 1281484800000, :clock? :utc}
        {:utc 1284076800000, :clock? :utc}]
       {:day 10, :time {:hour 24, :minute 0}, :month 8, :save 0}]
      [[{:utc 1284076800000, :clock? :utc}
        {:utc 1285804800000, :clock? :utc}]
       {:day 9, :time {:hour 24, :minute 0}, :month 9, :save 3600000}]
      [[{:utc 1285804800000, :clock? :utc}
        {:utc 1400198400000, :clock? :utc}]
       {:floating-day "lastThu",
        :time {:hour 24, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 1400198400000, :clock? :utc}
        {:utc 1403827200000, :clock? :utc}]
       {:day 15, :time {:hour 24, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 1403827200000, :clock? :utc}
        {:utc 1406851200000, :clock? :utc}]
       {:day 26, :time {:hour 24, :minute 0}, :month 6, :save 0}]
      [[{:utc 1406851200000, :clock? :utc}
        {:utc 1411603200000, :clock? :utc}]
       {:day 31,
        :time {:hour 24, :minute 0},
        :month 7,
        :save 3600000}]]},
    "ROK"
    {:current nil,
     :history
     [[[{:utc -681177600000, :clock? :utc}
        {:utc -672192000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -672192000000, :clock? :utc}
        {:utc -654739200000, :clock? :utc}]
       {:day 13, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -654739200000, :clock? :utc}
        {:utc -641088000000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -623376000000, :clock? :utc}
        {:utc -641088000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -641088000000, :clock? :utc}
        {:utc -588816000000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -588816000000, :clock? :utc}
        {:utc -451699200000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -462672000000, :clock? :utc}
        {:utc -451699200000, :clock? :utc}]
       {:day 5, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -451699200000, :clock? :utc}
        {:utc -429753600000, :clock? :utc}]
       {:day 9, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -429753600000, :clock? :utc}
        {:utc -418262400000, :clock? :utc}]
       {:day 20, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -418262400000, :clock? :utc}
        {:utc -399859200000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -399859200000, :clock? :utc}
        {:utc -387763200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc -387763200000, :clock? :utc}
        {:utc 547430400000, :clock? :utc}]
       {:floating-day "Sun>=18",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 547430400000, :clock? :utc}
        {:utc 560649600000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 2, :minute 0},
        :month 5,
        :save 3600000}]]},
    "Syria"
    {:current
     {:daylight-savings
      {:from 1333065600000,
       :clock? :utc,
       :floating-day "lastFri",
       :time {:hour 0, :minute 0},
       :month 3,
       :save 3600000},
      :standard
      {:from 1256860800000,
       :clock? :utc,
       :floating-day "lastFri",
       :time {:hour 0, :minute 0},
       :month 10,
       :save 0}},
     :history
     [[[{:utc -1568851200000, :clock? :utc}
        {:utc -1554249600000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -1554249600000, :clock? :utc}
        {:utc -242258400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -242258400000, :clock? :utc}
        {:utc -228866400000, :clock? :utc}]
       {:day 29, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -228866400000, :clock? :utc}
        {:utc -210549600000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -197416800000, :clock? :utc}
        {:utc -210549600000, :clock? :utc}]
       {:day 30, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc -165708000000, :clock? :utc}
        {:utc -210549600000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -210549600000, :clock? :utc}
        {:utc -134258400000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -134258400000, :clock? :utc}
        {:utc -116460000000, :clock? :utc}]
       {:day 30, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc -116460000000, :clock? :utc}
        {:utc -102636000000, :clock? :utc}]
       {:day 24, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -102636000000, :clock? :utc}
        {:utc -84319200000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -84319200000, :clock? :utc}
        {:utc 241927200000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 241927200000, :clock? :utc}
        {:utc 418701600000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc 418701600000, :clock? :utc}
        {:utc 433821600000, :clock? :utc}]
       {:day 9, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 433821600000, :clock? :utc}
        {:utc 508903200000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc 508903200000, :clock? :utc}
        {:utc 529207200000, :clock? :utc}]
       {:day 16, :time {:hour 2, :minute 0}, :month 2, :save 3600000}]
      [[{:utc 529207200000, :clock? :utc}
        {:utc 541562400000, :clock? :utc}]
       {:day 9, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc 541562400000, :clock? :utc}
        {:utc 562644000000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 562644000000, :clock? :utc}
        {:utc 574394400000, :clock? :utc}]
       {:day 31, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc 574394400000, :clock? :utc}
        {:utc 623210400000, :clock? :utc}]
       {:day 15, :time {:hour 2, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 607312800000, :clock? :utc}
        {:utc 623210400000, :clock? :utc}]
       {:day 31, :time {:hour 2, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 623210400000, :clock? :utc}
        {:utc 638935200000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc 638935200000, :clock? :utc}
        {:utc 654660000000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 654660000000, :clock? :utc}
        {:utc 670464000000, :clock? :utc}]
       {:day 30, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc 670464000000, :clock? :utc}
        {:utc 686275200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 686275200000, :clock? :utc}
        {:utc 702691200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 702691200000, :clock? :utc}
        {:utc 748915200000, :clock? :utc}]
       {:day 8, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 733104000000, :clock? :utc}
        {:utc 748915200000, :clock? :utc}]
       {:day 26, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 748915200000, :clock? :utc}
        {:utc 765158400000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 765158400000, :clock? :utc}
        {:utc 780969600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 859766400000, :clock? :utc}
        {:utc 780969600000, :clock? :utc}]
       {:floating-day "lastMon",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 780969600000, :clock? :utc}
        {:utc 922924800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 922924800000, :clock? :utc}
        {:utc 1158883200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 1158883200000, :clock? :utc}
        {:utc 1175212800000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1175212800000, :clock? :utc}
        {:utc 1193875200000, :clock? :utc}]
       {:floating-day "lastFri",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 1193875200000, :clock? :utc}
        {:utc 1207008000000, :clock? :utc}]
       {:floating-day "Fri>=1",
        :time {:hour 0, :minute 0},
        :month 11,
        :save 0}]
      [[{:utc 1207008000000, :clock? :utc}
        {:utc 1225497600000, :clock? :utc}]
       {:floating-day "Fri>=1",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 1225497600000, :clock? :utc}
        {:utc 1238112000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc 1238112000000, :clock? :utc}
        {:utc 1256860800000, :clock? :utc}]
       {:floating-day "lastFri",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 1270080000000, :clock? :utc}
        {:utc 1256860800000, :clock? :utc}]
       {:floating-day "Fri>=1",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Regina"
    {:current nil,
     :history
     [[[{:utc -1632088800000, :clock? :utc}
        {:utc -1615154400000, :clock? :utc}]
       {:day 14, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1615154400000, :clock? :utc}
        {:utc -1251936000000, :clock? :utc}]
       {:day 27, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -1251936000000, :clock? :utc}
        {:utc -1238716800000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc -1238716800000, :clock? :utc}
        {:utc -1032998400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -1017187200000, :clock? :utc}
        {:utc -1032998400000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -986256000000, :clock? :utc}
        {:utc -1032998400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -1032998400000, :clock? :utc}
        {:utc -954115200000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -954115200000, :clock? :utc}
        {:utc -880236000000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -880236000000, :clock? :utc}
        {:utc -765417600000, :clock? :utc}]
       {:day 9, :time {:hour 2, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -769395600000, :clock? :utc}
        {:utc -765417600000, :clock? :utc}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "u"},
        :month 8,
        :save 3600000}]
      [[{:utc -765417600000, :clock? :utc}
        {:utc -749001600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -749001600000, :clock? :utc}
        {:utc -733190400000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -733190400000, :clock? :utc}
        {:utc -715824000000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -715824000000, :clock? :utc}
        {:utc -702518400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -702518400000, :clock? :utc}
        {:utc -337219200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -337219200000, :clock? :utc}
        {:utc -321494400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Japan"
    {:current nil,
     :history
     [[[{:utc -683856000000, :clock? :utc}
        {:utc -672537600000, :clock? :utc}]
       {:floating-day "Sat>=1",
        :time {:hour 24, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc -654912000000, :clock? :utc}
        {:utc -672537600000, :clock? :utc}]
       {:floating-day "Sat>=1",
        :time {:hour 24, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -672537600000, :clock? :utc}
        {:utc -620784000000, :clock? :utc}]
       {:floating-day "Sun>=9",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]]},
    "AN"
    {:current
     {:standard
      {:from 1207008000000,
       :clock? :standard,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0, :time-suffix "s"},
       :month 4,
       :save 0},
      :daylight-savings
      {:from 1222819200000,
       :clock? :standard,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0, :time-suffix "s"},
       :month 10,
       :save 3600000}},
     :history
     [[[{:utc 68004000000, :clock? :standard}
        {:utc 57715200000, :clock? :standard}]
       {:day 27,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 2,
        :save 0}]
      [[{:utc 99792000000, :clock? :standard}
        {:utc 57715200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 386467200000, :clock? :standard}
        {:utc 57715200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 0}]
      [[{:utc 57715200000, :clock? :standard}
        {:utc 415324800000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 415324800000, :clock? :standard}
        {:utc 530071200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 530071200000, :clock? :standard}
        {:utc 511228800000, :clock? :standard}]
       {:day 19,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 511228800000, :clock? :standard}
        {:utc 562118400000, :clock? :standard}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 636249600000, :clock? :standard}
        {:utc 562118400000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 562118400000, :clock? :standard}
        {:utc 828230400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 967334400000, :clock? :standard}
        {:utc 828230400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 8,
        :save 3600000}]
      [[{:utc 828230400000, :clock? :standard}
        {:utc 1004227200000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 1143849600000, :clock? :standard}
        {:utc 1004227200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 0}]
      [[{:utc 1004227200000, :clock? :standard}
        {:utc 1174780800000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 1174780800000, :clock? :standard}
        {:utc 1222819200000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]]},
    "Ecuador"
    {:current nil,
     :history
     [[[{:utc 722908800000, :clock? :utc}
        {:utc 728870400000, :clock? :utc}]
       {:day 28,
        :time {:hour 0, :minute 0},
        :month 11,
        :save 3600000}]]},
    "Zion"
    {:current
     {:daylight-savings
      {:from 1363996800000,
       :clock? :utc,
       :floating-day "Fri>=23",
       :time {:hour 2, :minute 0},
       :month 3,
       :save 3600000},
      :standard
      {:from 1382832000000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 2, :minute 0},
       :month 10,
       :save 0}},
     :history
     [[[{:utc -933638400000, :clock? :utc}
        {:utc -857347200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -844293600000, :clock? :utc}
        {:utc -857347200000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -857347200000, :clock? :utc}
        {:utc -812678400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc -812678400000, :clock? :utc}
        {:utc -762645600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -779846400000, :clock? :utc}
        {:utc -762645600000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -762645600000, :clock? :utc}
        {:utc -748303200000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 11, :save 0}]
      [[{:utc -748303200000, :clock? :utc}
        {:utc -731116800000, :clock? :utc}]
       {:day 16, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -731116800000, :clock? :utc}
        {:utc -681955200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc -681955200000, :clock? :utc}
        {:utc -667951200000, :clock? :utc}]
       {:day 23, :time {:hour 0, :minute 0}, :month 5, :save 7200000}]
      [[{:utc -673228800000, :clock? :utc}
        {:utc -667951200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 9, :save 3600000}]
      [[{:utc -667951200000, :clock? :utc}
        {:utc -652320000000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 11, :save 0}]
      [[{:utc -652320000000, :clock? :utc}
        {:utc -608936400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -622080000000, :clock? :utc}
        {:utc -608936400000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -608936400000, :clock? :utc}
        {:utc -591840000000, :clock? :utc}]
       {:day 15, :time {:hour 3, :minute 0}, :month 9, :save 0}]
      [[{:utc -591840000000, :clock? :utc}
        {:utc -572475600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -572475600000, :clock? :utc}
        {:utc -558568800000, :clock? :utc}]
       {:day 11, :time {:hour 3, :minute 0}, :month 11, :save 0}]
      [[{:utc -558568800000, :clock? :utc}
        {:utc -542840400000, :clock? :utc}]
       {:day 20, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -542840400000, :clock? :utc}
        {:utc -527724000000, :clock? :utc}]
       {:day 19, :time {:hour 3, :minute 0}, :month 10, :save 0}]
      [[{:utc -527724000000, :clock? :utc}
        {:utc -514414800000, :clock? :utc}]
       {:day 12, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -514414800000, :clock? :utc}
        {:utc -490838400000, :clock? :utc}]
       {:day 13, :time {:hour 3, :minute 0}, :month 9, :save 0}]
      [[{:utc -490838400000, :clock? :utc}
        {:utc -482976000000, :clock? :utc}]
       {:day 13, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -482976000000, :clock? :utc}
        {:utc -459468000000, :clock? :utc}]
       {:day 12, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -459468000000, :clock? :utc}
        {:utc -451526400000, :clock? :utc}]
       {:day 11, :time {:hour 2, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -451526400000, :clock? :utc}
        {:utc -428544000000, :clock? :utc}]
       {:day 11, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -428544000000, :clock? :utc}
        {:utc -418251600000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -418251600000, :clock? :utc}
        {:utc -400024800000, :clock? :utc}]
       {:day 30, :time {:hour 3, :minute 0}, :month 9, :save 0}]
      [[{:utc -400024800000, :clock? :utc}
        {:utc -387417600000, :clock? :utc}]
       {:day 29, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -387417600000, :clock? :utc}
        {:utc 142387200000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 142387200000, :clock? :utc}
        {:utc 150854400000, :clock? :utc}]
       {:day 7, :time {:hour 0, :minute 0}, :month 7, :save 3600000}]
      [[{:utc 150854400000, :clock? :utc}
        {:utc 167184000000, :clock? :utc}]
       {:day 13, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 167184000000, :clock? :utc}
        {:utc 178675200000, :clock? :utc}]
       {:day 20, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 178675200000, :clock? :utc}
        {:utc 482284800000, :clock? :utc}]
       {:day 31, :time {:hour 0, :minute 0}, :month 8, :save 0}]
      [[{:utc 482284800000, :clock? :utc}
        {:utc 495590400000, :clock? :utc}]
       {:day 14, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 495590400000, :clock? :utc}
        {:utc 516758400000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 516758400000, :clock? :utc}
        {:utc 526435200000, :clock? :utc}]
       {:day 18, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 526435200000, :clock? :utc}
        {:utc 545443200000, :clock? :utc}]
       {:day 7, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 545443200000, :clock? :utc}
        {:utc 558489600000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 558489600000, :clock? :utc}
        {:utc 576633600000, :clock? :utc}]
       {:day 13, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 576633600000, :clock? :utc}
        {:utc 589334400000, :clock? :utc}]
       {:day 10, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 589334400000, :clock? :utc}
        {:utc 609897600000, :clock? :utc}]
       {:day 4, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 609897600000, :clock? :utc}
        {:utc 620784000000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 620784000000, :clock? :utc}
        {:utc 638323200000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 638323200000, :clock? :utc}
        {:utc 651628800000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 651628800000, :clock? :utc}
        {:utc 669772800000, :clock? :utc}]
       {:day 26, :time {:hour 0, :minute 0}, :month 8, :save 0}]
      [[{:utc 669772800000, :clock? :utc}
        {:utc 683683200000, :clock? :utc}]
       {:day 24, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 683683200000, :clock? :utc}
        {:utc 701827200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 701827200000, :clock? :utc}
        {:utc 715737600000, :clock? :utc}]
       {:day 29, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 715737600000, :clock? :utc}
        {:utc 733708800000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 733708800000, :clock? :utc}
        {:utc 747187200000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 747187200000, :clock? :utc}
        {:utc 765158400000, :clock? :utc}]
       {:day 5, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 765158400000, :clock? :utc}
        {:utc 778032000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 778032000000, :clock? :utc}
        {:utc 796608000000, :clock? :utc}]
       {:day 28, :time {:hour 0, :minute 0}, :month 8, :save 0}]
      [[{:utc 796608000000, :clock? :utc}
        {:utc 810086400000, :clock? :utc}]
       {:day 31, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 810086400000, :clock? :utc}
        {:utc 826848000000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 826848000000, :clock? :utc}
        {:utc 842832000000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 842832000000, :clock? :utc}
        {:utc 858902400000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 858902400000, :clock? :utc}
        {:utc 874195200000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 874195200000, :clock? :utc}
        {:utc 890352000000, :clock? :utc}]
       {:day 14, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 890352000000, :clock? :utc}
        {:utc 905040000000, :clock? :utc}]
       {:day 20, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 905040000000, :clock? :utc}
        {:utc 923018400000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 923018400000, :clock? :utc}
        {:utc 936324000000, :clock? :utc}]
       {:day 2, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 936324000000, :clock? :utc}
        {:utc 955677600000, :clock? :utc}]
       {:day 3, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc 955677600000, :clock? :utc}
        {:utc 970794000000, :clock? :utc}]
       {:day 14, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 970794000000, :clock? :utc}
        {:utc 986778000000, :clock? :utc}]
       {:day 6, :time {:hour 1, :minute 0}, :month 10, :save 0}]
      [[{:utc 986778000000, :clock? :utc}
        {:utc 1001293200000, :clock? :utc}]
       {:day 9, :time {:hour 1, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 1001293200000, :clock? :utc}
        {:utc 1017363600000, :clock? :utc}]
       {:day 24, :time {:hour 1, :minute 0}, :month 9, :save 0}]
      [[{:utc 1017363600000, :clock? :utc}
        {:utc 1033952400000, :clock? :utc}]
       {:day 29, :time {:hour 1, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1033952400000, :clock? :utc}
        {:utc 1048813200000, :clock? :utc}]
       {:day 7, :time {:hour 1, :minute 0}, :month 10, :save 0}]
      [[{:utc 1048813200000, :clock? :utc}
        {:utc 1065142800000, :clock? :utc}]
       {:day 28, :time {:hour 1, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1065142800000, :clock? :utc}
        {:utc 1081299600000, :clock? :utc}]
       {:day 3, :time {:hour 1, :minute 0}, :month 10, :save 0}]
      [[{:utc 1081299600000, :clock? :utc}
        {:utc 1095814800000, :clock? :utc}]
       {:day 7, :time {:hour 1, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 1095814800000, :clock? :utc}
        {:utc 1112320800000, :clock? :utc}]
       {:day 22, :time {:hour 1, :minute 0}, :month 9, :save 0}]
      [[{:utc 1112320800000, :clock? :utc}
        {:utc 1128823200000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 1128823200000, :clock? :utc}
        {:utc 1143331200000, :clock? :utc}]
       {:day 9, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc 1159668000000, :clock? :utc}
        {:utc 1143331200000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc 1189908000000, :clock? :utc}
        {:utc 1143331200000, :clock? :utc}]
       {:day 16, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc 1223172000000, :clock? :utc}
        {:utc 1143331200000, :clock? :utc}]
       {:day 5, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc 1254016800000, :clock? :utc}
        {:utc 1143331200000, :clock? :utc}]
       {:day 27, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc 1143331200000, :clock? :utc}
        {:utc 1284256800000, :clock? :utc}]
       {:floating-day "Fri>=26",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 1284256800000, :clock? :utc}
        {:utc 1301623200000, :clock? :utc}]
       {:day 12, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc 1301623200000, :clock? :utc}
        {:utc 1317520800000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 1317520800000, :clock? :utc}
        {:utc 1332720000000, :clock? :utc}]
       {:day 2, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc 1332720000000, :clock? :utc}
        {:utc 1348365600000, :clock? :utc}]
       {:floating-day "Fri>=26",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 1348365600000, :clock? :utc}
        {:utc 1363996800000, :clock? :utc}]
       {:day 23, :time {:hour 2, :minute 0}, :month 9, :save 0}]]},
    "Ghana"
    {:current nil,
     :history
     [[[{:utc -1556841600000, :clock? :utc}
        {:utc -1546387200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 9, :save 1200000}]]},
    "Latvia"
    {:current nil,
     :history
     [[[{:utc 606873600000, :clock? :standard}
        {:utc 622598400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]]},
    "Vincennes"
    {:current nil,
     :history
     [[[{:utc -747273600000, :clock? :utc}
        {:utc -733968000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -733968000000, :clock? :utc}
        {:utc -526521600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -526521600000, :clock? :utc}
        {:utc -513216000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -463017600000, :clock? :utc}
        {:utc -513216000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -513216000000, :clock? :utc}
        {:utc -431568000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -289440000000, :clock? :utc}
        {:utc -431568000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -261014400000, :clock? :utc}
        {:utc -431568000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -431568000000, :clock? :utc}
        {:utc -226540800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Namibia"
    {:current nil,
     :history
     [[[{:utc 764208000000, :clock? :utc}
        {:utc 778377600000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 3, :save -3600000}]
      [[{:utc 778377600000, :clock? :utc}
        {:utc 796694400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]]},
    "Tonga"
    {:current nil,
     :history
     [[[{:utc 939261600000, :clock? :standard}
        {:utc 953431200000, :clock? :standard}]
       {:day 7,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 953431200000, :clock? :standard}
        {:utc 973036800000, :clock? :utc}]
       {:day 19,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 973036800000, :clock? :utc}
        {:utc 980640000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 11,
        :save 3600000}]
      [[{:utc 980640000000, :clock? :utc}
        {:utc 1477958400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 1,
        :save 0}]
      [[{:utc 1477958400000, :clock? :utc}
        {:utc 1484438400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 11,
        :save 3600000}]]},
    "Marengo"
    {:current nil,
     :history
     [[[{:utc -589420800000, :clock? :utc}
        {:utc -576115200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -576115200000, :clock? :utc}
        {:utc -495072000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -495072000000, :clock? :utc}
        {:utc -481766400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Greece"
    {:current nil,
     :history
     [[[{:utc -1182988800000, :clock? :utc}
        {:utc -1178150400000, :clock? :utc}]
       {:day 7, :time {:hour 0, :minute 0}, :month 7, :save 3600000}]
      [[{:utc -1178150400000, :clock? :utc}
        {:utc -906854400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -906854400000, :clock? :utc}
        {:utc -857250000000, :clock? :utc}]
       {:day 7, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -857250000000, :clock? :utc}
        {:utc -844473600000, :clock? :utc}]
       {:day 2, :time {:hour 3, :minute 0}, :month 11, :save 0}]
      [[{:utc -844473600000, :clock? :utc}
        {:utc -828230400000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -828230400000, :clock? :utc}
        {:utc -552355200000, :clock? :utc}]
       {:day 4, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -552355200000, :clock? :utc}
        {:utc -541641600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 7, :save 3600000}]
      [[{:utc -541641600000, :clock? :utc}
        {:utc 166492800000, :clock? :standard}]
       {:day 2, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc 166492800000, :clock? :standard}
        {:utc 186192000000, :clock? :standard}]
       {:day 12,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 186192000000, :clock? :standard}
        {:utc 198036000000, :clock? :standard}]
       {:day 26,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 11,
        :save 0}]
      [[{:utc 198036000000, :clock? :standard}
        {:utc 213760800000, :clock? :standard}]
       {:day 11,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 213760800000, :clock? :standard}
        {:utc 228700800000, :clock? :standard}]
       {:day 10,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 244087200000, :clock? :standard}
        {:utc 228700800000, :clock? :standard}]
       {:day 26,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 228700800000, :clock? :standard}
        {:utc 275457600000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 275457600000, :clock? :utc}
        {:utc 291805200000, :clock? :utc}]
       {:day 24, :time {:hour 4, :minute 0}, :month 9, :save 0}]
      [[{:utc 291805200000, :clock? :utc}
        {:utc 307418400000, :clock? :utc}]
       {:day 1, :time {:hour 9, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 307418400000, :clock? :utc}
        {:utc 323395200000, :clock? :utc}]
       {:day 29, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc 323395200000, :clock? :utc}
        {:utc 338947200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]]},
    "Toronto"
    {:current nil,
     :history
     [[[{:utc -1601771400000, :clock? :utc}
        {:utc -1583712000000, :clock? :utc}]
       {:day 30, :time {:hour 23, :minute 30}, :month 3, :save 3600000}]
      [[{:utc -1583712000000, :clock? :utc}
        {:utc -1567375200000, :clock? :utc}]
       {:day 26, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -1567375200000, :clock? :utc}
        {:utc -1554681600000, :clock? :utc}]
       {:day 2, :time {:hour 2, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1554681600000, :clock? :utc}
        {:utc -1534716000000, :clock? :utc}]
       {:day 26, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -1534716000000, :clock? :utc}
        {:utc -1524088800000, :clock? :utc}]
       {:day 15, :time {:hour 2, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1524088800000, :clock? :utc}
        {:utc -1503792000000, :clock? :utc}]
       {:day 15, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc -1503792000000, :clock? :utc}
        {:utc -1492560000000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 2, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc -1492560000000, :clock? :utc}
        {:utc -1441238400000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -1441238400000, :clock? :utc}
        {:utc -1333929600000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc -1315180800000, :clock? :utc}
        {:utc -1333929600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -1333929600000, :clock? :utc}
        {:utc -1188770400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -1188770400000, :clock? :utc}
        {:utc -1144015200000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1144015200000, :clock? :utc}
        {:utc -1157328000000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -1112572800000, :clock? :utc}
        {:utc -1157328000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -1157328000000, :clock? :utc}
        {:utc -765417600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -765417600000, :clock? :utc}
        {:utc -747273600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -747273600000, :clock? :utc}
        {:utc -702518400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -702518400000, :clock? :utc}
        {:utc -715824000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -715824000000, :clock? :utc}
        {:utc -634176000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -634176000000, :clock? :utc}
        {:utc -620870400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 11,
        :save 0}]
      [[{:utc -602726400000, :clock? :utc}
        {:utc -620870400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 11,
        :save 0}]
      [[{:utc -576115200000, :clock? :utc}
        {:utc -620870400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -620870400000, :clock? :utc}
        {:utc -384393600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Swift"
    {:current nil,
     :history
     [[[{:utc -400118400000, :clock? :utc}
        {:utc -384393600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -384393600000, :clock? :utc}
        {:utc -337219200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -321494400000, :clock? :utc}
        {:utc -337219200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -337219200000, :clock? :utc}
        {:utc -292464000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Vanc"
    {:current nil,
     :history
     [[[{:utc -1632088800000, :clock? :utc}
        {:utc -1615154400000, :clock? :utc}]
       {:day 14, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1615154400000, :clock? :utc}
        {:utc -880236000000, :clock? :utc}]
       {:day 27, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -880236000000, :clock? :utc}
        {:utc -765410400000, :clock? :utc}]
       {:day 9, :time {:hour 2, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -769395600000, :clock? :utc}
        {:utc -765410400000, :clock? :utc}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "u"},
        :month 8,
        :save 3600000}]
      [[{:utc -765410400000, :clock? :utc}
        {:utc -747273600000, :clock? :utc}]
       {:day 30, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc -732751200000, :clock? :utc}
        {:utc -747273600000, :clock? :utc}]
       {:day 13, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -702518400000, :clock? :utc}
        {:utc -747273600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -747273600000, :clock? :utc}
        {:utc -226540800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Russia"
    {:current nil,
     :history
     [[[{:utc -1656810000000, :clock? :utc}
        {:utc -1641340800000, :clock? :utc}]
       {:day 1, :time {:hour 23, :minute 0}, :month 7, :save 3600000}]
      [[{:utc -1641340800000, :clock? :utc}
        {:utc -1627956000000, :clock? :utc}]
       {:day 28, :time {:hour 0, :minute 0}, :month 12, :save 0}]
      [[{:utc -1627956000000, :clock? :utc}
        {:utc -1589846400000, :clock? :utc}]
       {:day 31, :time {:hour 22, :minute 0}, :month 5, :save 7200000}]
      [[{:utc -1618700400000, :clock? :utc}
        {:utc -1589846400000, :clock? :utc}]
       {:day 16, :time {:hour 1, :minute 0}, :month 9, :save 3600000}]
      [[{:utc -1596416400000, :clock? :utc}
        {:utc -1589846400000, :clock? :utc}]
       {:day 31, :time {:hour 23, :minute 0}, :month 5, :save 7200000}]
      [[{:utc -1593820800000, :clock? :utc}
        {:utc -1589846400000, :clock? :utc}]
       {:day 1,
        :time {:hour 0, :minute 0, :time-suffix "u"},
        :month 7,
        :save 3600000}]
      [[{:utc -1589846400000, :clock? :utc}
        {:utc -1542416400000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 8, :save 0}]
      [[{:utc -1542416400000, :clock? :utc}
        {:utc -1522713600000, :clock? :utc}]
       {:day 14, :time {:hour 23, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -1539478800000, :clock? :utc}
        {:utc -1522713600000, :clock? :utc}]
       {:day 20, :time {:hour 23, :minute 0}, :month 3, :save 7200000}]
      [[{:utc -1525305600000, :clock? :utc}
        {:utc -1522713600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 9, :save 3600000}]
      [[{:utc -1522713600000, :clock? :utc}
        {:utc 354931200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 370742400000, :clock? :utc}
        {:utc 354931200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 354931200000, :clock? :utc}
        {:utc 465350400000, :clock? :standard}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 465350400000, :clock? :standard}
        {:utc 481075200000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 481075200000, :clock? :standard}
        {:utc 846374400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]]},
    "Italy"
    {:current nil,
     :history
     [[[{:utc -1690761600000, :clock? :utc}
        {:utc -1680480000000, :clock? :utc}]
       {:day 3, :time {:hour 24, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -1680480000000, :clock? :utc}
        {:utc -1664755200000, :clock? :utc}]
       {:day 30, :time {:hour 24, :minute 0}, :month 9, :save 0}]
      [[{:utc -1664755200000, :clock? :utc}
        {:utc -1616889600000, :clock? :utc}]
       {:day 31, :time {:hour 24, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -1635120000000, :clock? :utc}
        {:utc -1616889600000, :clock? :utc}]
       {:day 9, :time {:hour 24, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -1616889600000, :clock? :utc}
        {:utc -1604275200000, :clock? :utc}]
       {:day 6, :time {:hour 24, :minute 0}, :month 10, :save 0}]
      [[{:utc -1604275200000, :clock? :utc}
        {:utc -1585526400000, :clock? :utc}]
       {:day 1, :time {:hour 24, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -1585526400000, :clock? :utc}
        {:utc -1571011200000, :clock? :utc}]
       {:day 4, :time {:hour 24, :minute 0}, :month 10, :save 0}]
      [[{:utc -1571011200000, :clock? :utc}
        {:utc -1555286400000, :clock? :utc}]
       {:day 20, :time {:hour 24, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -1555286400000, :clock? :utc}
        {:utc -932428800000, :clock? :utc}]
       {:day 18, :time {:hour 24, :minute 0}, :month 9, :save 0}]
      [[{:utc -932428800000, :clock? :utc}
        {:utc -857253600000, :clock? :standard}]
       {:day 14, :time {:hour 24, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -857253600000, :clock? :standard}
        {:utc -844556400000, :clock? :standard}]
       {:day 2,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 11,
        :save 0}]
      [[{:utc -844556400000, :clock? :standard}
        {:utc -828223200000, :clock? :standard}]
       {:day 29,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -828223200000, :clock? :standard}
        {:utc -812584800000, :clock? :standard}]
       {:day 4,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -812584800000, :clock? :standard}
        {:utc -798069600000, :clock? :standard}]
       {:day 2,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -798069600000, :clock? :standard}
        {:utc -781048800000, :clock? :utc}]
       {:day 17,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -781048800000, :clock? :utc}
        {:utc -766710000000, :clock? :utc}]
       {:day 2, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -766710000000, :clock? :utc}
        {:utc -750895200000, :clock? :standard}]
       {:day 15, :time {:hour 1, :minute 0}, :month 9, :save 0}]
      [[{:utc -750895200000, :clock? :standard}
        {:utc -733356000000, :clock? :standard}]
       {:day 17,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -733356000000, :clock? :standard}
        {:utc -719452800000, :clock? :standard}]
       {:day 6,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -719452800000, :clock? :standard}
        {:utc -701913600000, :clock? :standard}]
       {:day 16,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -701913600000, :clock? :standard}
        {:utc -689205600000, :clock? :standard}]
       {:day 5,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -689205600000, :clock? :standard}
        {:utc -670456800000, :clock? :standard}]
       {:day 29,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 2,
        :save 3600000}]
      [[{:utc -670456800000, :clock? :standard}
        {:utc -114048000000, :clock? :standard}]
       {:day 3,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -103161600000, :clock? :utc}
        {:utc -114048000000, :clock? :standard}]
       {:day 24, :time {:hour 24, :minute 0}, :month 9, :save 0}]
      [[{:utc -114048000000, :clock? :standard}
        {:utc -71884800000, :clock? :standard}]
       {:floating-day "Sun>=22",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc -71884800000, :clock? :standard}
        {:utc -18489600000, :clock? :standard}]
       {:floating-day "Sun>=22",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -18489600000, :clock? :standard}
        {:utc 23241600000, :clock? :standard}]
       {:day 1,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 6,
        :save 3600000}]
      [[{:utc 12960000000, :clock? :standard}
        {:utc 23241600000, :clock? :standard}]
       {:day 31,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc 23241600000, :clock? :standard}
        {:utc 43718400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 54691200000, :clock? :standard}
        {:utc 43718400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 43718400000, :clock? :standard}
        {:utc 86745600000, :clock? :standard}]
       {:floating-day "Sun>=22",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc 86745600000, :clock? :standard}
        {:utc 107913600000, :clock? :standard}]
       {:day 1,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 107913600000, :clock? :standard}
        {:utc 118195200000, :clock? :standard}]
       {:day 3,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 6,
        :save 3600000}]
      [[{:utc 118195200000, :clock? :standard}
        {:utc 138758400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 138758400000, :clock? :standard}
        {:utc 181094400000, :clock? :standard}]
       {:day 26,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc 170812800000, :clock? :standard}
        {:utc 181094400000, :clock? :standard}]
       {:day 1,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 6,
        :save 3600000}]
      [[{:utc 202262400000, :clock? :standard}
        {:utc 181094400000, :clock? :standard}]
       {:day 30,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc 181094400000, :clock? :standard}
        {:utc 233107200000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 276048000000, :clock? :standard}
        {:utc 233107200000, :clock? :standard}]
       {:day 1,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 233107200000, :clock? :standard}
        {:utc 307497600000, :clock? :standard}]
       {:floating-day "Sun>=22",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]]},
    "Belize"
    {:current nil,
     :history
     [[[{:utc -1617321600000, :clock? :utc}
        {:utc -1606089600000, :clock? :utc}]
       {:floating-day "Sun>=2",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 1800000}]
      [[{:utc -1606089600000, :clock? :utc}
        {:utc 123897600000, :clock? :utc}]
       {:floating-day "Sun>=9",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 123897600000, :clock? :utc}
        {:utc 129600000000, :clock? :utc}]
       {:day 5, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc 129600000000, :clock? :utc}
        {:utc 409017600000, :clock? :utc}]
       {:day 9, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 409017600000, :clock? :utc}
        {:utc 413856000000, :clock? :utc}]
       {:day 18,
        :time {:hour 0, :minute 0},
        :month 12,
        :save 3600000}]]},
    "France"
    {:current nil,
     :history
     [[[{:utc -1689814800000, :clock? :standard}
        {:utc -1680480000000, :clock? :standard}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 6,
        :save 3600000}]
      [[{:utc -1665363600000, :clock? :standard}
        {:utc -1680480000000, :clock? :standard}]
       {:day 24,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1635123600000, :clock? :standard}
        {:utc -1680480000000, :clock? :standard}]
       {:day 9,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1680480000000, :clock? :standard}
        {:utc -1604278800000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1604278800000, :clock? :standard}
        {:utc -1552266000000, :clock? :standard}]
       {:day 1,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1574038800000, :clock? :standard}
        {:utc -1552266000000, :clock? :standard}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 2,
        :save 3600000}]
      [[{:utc -1552266000000, :clock? :standard}
        {:utc -1539997200000, :clock? :standard}]
       {:day 23,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1539997200000, :clock? :standard}
        {:utc -1520557200000, :clock? :standard}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1520557200000, :clock? :standard}
        {:utc -1507510800000, :clock? :standard}]
       {:day 25,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1507510800000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 25,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1470618000000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 26,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc -1444006800000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 29,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1411952400000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 4,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1379293200000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 17,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1348448400000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 9,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1316394000000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1284339600000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 20,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1253494800000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 12,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1221440400000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 18,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1191200400000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 2,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1160355600000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 25,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1127696400000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 7,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1096851600000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 30,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1063587600000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 18,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1033347600000, :clock? :standard}
        {:utc -1491177600000, :clock? :standard}]
       {:day 3,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1491177600000, :clock? :standard}
        {:utc -1002502800000, :clock? :standard}]
       {:floating-day "Sat>=1",
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1002502800000, :clock? :standard}
        {:utc -950490000000, :clock? :standard}]
       {:day 26,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -969238800000, :clock? :standard}
        {:utc -950490000000, :clock? :standard}]
       {:day 15,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -950490000000, :clock? :standard}
        {:utc -942012000000, :clock? :utc}]
       {:day 18,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 11,
        :save 0}]
      [[{:utc -942012000000, :clock? :utc}
        {:utc -766616400000, :clock? :utc}]
       {:day 25, :time {:hour 2, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -904435200000, :clock? :utc}
        {:utc -766616400000, :clock? :utc}]
       {:day 5, :time {:hour 0, :minute 0}, :month 5, :save 7200000}]
      [[{:utc -891129600000, :clock? :utc}
        {:utc -766616400000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -877824000000, :clock? :utc}
        {:utc -766616400000, :clock? :utc}]
       {:day 9, :time {:hour 0, :minute 0}, :month 3, :save 7200000}]
      [[{:utc -857250000000, :clock? :utc}
        {:utc -766616400000, :clock? :utc}]
       {:day 2, :time {:hour 3, :minute 0}, :month 11, :save 3600000}]
      [[{:utc -844556400000, :clock? :utc}
        {:utc -766616400000, :clock? :utc}]
       {:day 29, :time {:hour 2, :minute 0}, :month 3, :save 7200000}]
      [[{:utc -828219600000, :clock? :utc}
        {:utc -766616400000, :clock? :utc}]
       {:day 4, :time {:hour 3, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -812502000000, :clock? :utc}
        {:utc -766616400000, :clock? :utc}]
       {:day 3, :time {:hour 2, :minute 0}, :month 4, :save 7200000}]
      [[{:utc -796258800000, :clock? :utc}
        {:utc -766616400000, :clock? :utc}]
       {:day 8, :time {:hour 1, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -781048800000, :clock? :utc}
        {:utc -766616400000, :clock? :utc}]
       {:day 2, :time {:hour 2, :minute 0}, :month 4, :save 7200000}]
      [[{:utc -766616400000, :clock? :utc}
        {:utc 196822800000, :clock? :utc}]
       {:day 16, :time {:hour 3, :minute 0}, :month 9, :save 0}]
      [[{:utc 196822800000, :clock? :utc}
        {:utc 212547600000, :clock? :utc}]
       {:day 28, :time {:hour 1, :minute 0}, :month 3, :save 3600000}]]},
    "NZ"
    {:current
     {:daylight-savings
      {:from 1191110400000,
       :clock? :standard,
       :floating-day "lastSun",
       :time {:hour 2, :minute 0, :time-suffix "s"},
       :month 9,
       :save 3600000},
      :standard
      {:from 1207008000000,
       :clock? :standard,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0, :time-suffix "s"},
       :month 4,
       :save 0}},
     :history
     [[[{:utc -1330293600000, :clock? :utc}
        {:utc -1320012000000, :clock? :utc}]
       {:day 6, :time {:hour 2, :minute 0}, :month 11, :save 3600000}]
      [[{:utc -1320012000000, :clock? :utc}
        {:utc -1301184000000, :clock? :utc}]
       {:day 4, :time {:hour 2, :minute 0}, :month 3, :save 0}]
      [[{:utc -1301184000000, :clock? :utc}
        {:utc -1287532800000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 1800000}]
      [[{:utc -1287532800000, :clock? :utc}
        {:utc -1112572800000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc -1125878400000, :clock? :utc}
        {:utc -1112572800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 0}]
      [[{:utc -1112572800000, :clock? :utc}
        {:utc -757382400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 1800000}]
      [[{:utc -757382400000, :clock? :utc}
        {:utc 152496000000, :clock? :standard}]
       {:day 1, :time {:hour 0, :minute 0}, :month 1, :save 0}]
      [[{:utc 152496000000, :clock? :standard}
        {:utc 162345600000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 11,
        :save 3600000}]
      [[{:utc 162345600000, :clock? :standard}
        {:utc 183513600000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 2,
        :save 0}]
      [[{:utc 183513600000, :clock? :standard}
        {:utc 194486400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 194486400000, :clock? :standard}
        {:utc 623808000000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 623808000000, :clock? :standard}
        {:utc 637459200000, :clock? :standard}]
       {:floating-day "Sun>=8",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 654739200000, :clock? :standard}
        {:utc 637459200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 637459200000, :clock? :standard}
        {:utc 1191110400000, :clock? :standard}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]]},
    "AW"
    {:current nil,
     :history
     [[[{:utc 152064000000, :clock? :standard}
        {:utc 162864000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 162864000000, :clock? :standard}
        {:utc 436320000000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 436320000000, :clock? :standard}
        {:utc 446947200000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 446947200000, :clock? :standard}
        {:utc 690343200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 690343200000, :clock? :standard}
        {:utc 699408000000, :clock? :standard}]
       {:day 17,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 11,
        :save 3600000}]
      [[{:utc 699408000000, :clock? :standard}
        {:utc 1165111200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 1165111200000, :clock? :standard}
        {:utc 1174780800000, :clock? :standard}]
       {:day 3,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 12,
        :save 3600000}]
      [[{:utc 1193529600000, :clock? :standard}
        {:utc 1174780800000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]]},
    "Iran"
    {:current
     {:daylight-savings
      {:from 2089670400000,
       :clock? :utc,
       :day 21,
       :time {:hour 0, :minute 0},
       :month 3,
       :save 3600000},
      :standard
      {:from 2105568000000,
       :clock? :utc,
       :day 21,
       :time {:hour 0, :minute 0},
       :month 9,
       :save 0}},
     :history
     [[[{:utc 277776000000, :clock? :utc}
        {:utc 259286400000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 306547200000, :clock? :utc}
        {:utc 259286400000, :clock? :utc}]
       {:day 19, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 259286400000, :clock? :utc}
        {:utc 338515200000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 338515200000, :clock? :utc}
        {:utc 673228800000, :clock? :utc}]
       {:day 23, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 673228800000, :clock? :utc}
        {:utc 685497600000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 701222400000, :clock? :utc}
        {:utc 685497600000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 685497600000, :clock? :utc}
        {:utc 827366400000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 827366400000, :clock? :utc}
        {:utc 843264000000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 843264000000, :clock? :utc}
        {:utc 858988800000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 858988800000, :clock? :utc}
        {:utc 874886400000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 874886400000, :clock? :utc}
        {:utc 953596800000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 953596800000, :clock? :utc}
        {:utc 969494400000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 969494400000, :clock? :utc}
        {:utc 985219200000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 985219200000, :clock? :utc}
        {:utc 1001116800000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1001116800000, :clock? :utc}
        {:utc 1079827200000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1079827200000, :clock? :utc}
        {:utc 1095724800000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1095724800000, :clock? :utc}
        {:utc 1111449600000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1111449600000, :clock? :utc}
        {:utc 1127347200000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1127347200000, :clock? :utc}
        {:utc 1206057600000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1206057600000, :clock? :utc}
        {:utc 1221955200000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1221955200000, :clock? :utc}
        {:utc 1237680000000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1237680000000, :clock? :utc}
        {:utc 1253577600000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1253577600000, :clock? :utc}
        {:utc 1332288000000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1332288000000, :clock? :utc}
        {:utc 1348185600000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1348185600000, :clock? :utc}
        {:utc 1363910400000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1363910400000, :clock? :utc}
        {:utc 1379808000000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1379808000000, :clock? :utc}
        {:utc 1458518400000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1458518400000, :clock? :utc}
        {:utc 1474416000000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1474416000000, :clock? :utc}
        {:utc 1490140800000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1490140800000, :clock? :utc}
        {:utc 1506038400000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1506038400000, :clock? :utc}
        {:utc 1584748800000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1584748800000, :clock? :utc}
        {:utc 1600646400000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1600646400000, :clock? :utc}
        {:utc 1616371200000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1616371200000, :clock? :utc}
        {:utc 1632268800000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1632268800000, :clock? :utc}
        {:utc 1710979200000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1710979200000, :clock? :utc}
        {:utc 1726876800000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1726876800000, :clock? :utc}
        {:utc 1742601600000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1742601600000, :clock? :utc}
        {:utc 1758499200000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1758499200000, :clock? :utc}
        {:utc 1837209600000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1837209600000, :clock? :utc}
        {:utc 1853107200000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1853107200000, :clock? :utc}
        {:utc 1900368000000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1900368000000, :clock? :utc}
        {:utc 1916265600000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1916265600000, :clock? :utc}
        {:utc 1963440000000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1963440000000, :clock? :utc}
        {:utc 1979337600000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 1979337600000, :clock? :utc}
        {:utc 2026598400000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 2026598400000, :clock? :utc}
        {:utc 2042496000000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 2042496000000, :clock? :utc}
        {:utc 2089670400000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]]},
    "Port"
    {:current nil,
     :history
     [[[{:utc -1689555600000, :clock? :utc}
        {:utc -1677798000000, :clock? :utc}]
       {:day 17, :time {:hour 23, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -1677798000000, :clock? :utc}
        {:utc -1667437200000, :clock? :standard}]
       {:day 1, :time {:hour 1, :minute 0}, :month 11, :save 0}]
      [[{:utc -1667437200000, :clock? :standard}
        {:utc -1647738000000, :clock? :standard}]
       {:day 28,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 2,
        :save 3600000}]
      [[{:utc -1635814800000, :clock? :standard}
        {:utc -1647738000000, :clock? :standard}]
       {:day 1,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1604365200000, :clock? :standard}
        {:utc -1647738000000, :clock? :standard}]
       {:day 28,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 2,
        :save 3600000}]
      [[{:utc -1572742800000, :clock? :standard}
        {:utc -1647738000000, :clock? :standard}]
       {:day 29,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 2,
        :save 3600000}]
      [[{:utc -1647738000000, :clock? :standard}
        {:utc -1541206800000, :clock? :standard}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1541206800000, :clock? :standard}
        {:utc -1426813200000, :clock? :standard}]
       {:day 28,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 2,
        :save 3600000}]
      [[{:utc -1442451600000, :clock? :standard}
        {:utc -1426813200000, :clock? :standard}]
       {:day 16,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1426813200000, :clock? :standard}
        {:utc -1379293200000, :clock? :standard}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1379293200000, :clock? :standard}
        {:utc -1364947200000, :clock? :standard}]
       {:day 17,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1348448400000, :clock? :standard}
        {:utc -1364947200000, :clock? :standard}]
       {:day 9,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1316394000000, :clock? :standard}
        {:utc -1364947200000, :clock? :standard}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1364947200000, :clock? :standard}
        {:utc -1284339600000, :clock? :standard}]
       {:floating-day "Sat>=1",
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1284339600000, :clock? :standard}
        {:utc -1207180800000, :clock? :standard}]
       {:day 20,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1221440400000, :clock? :standard}
        {:utc -1207180800000, :clock? :standard}]
       {:day 18,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1207180800000, :clock? :standard}
        {:utc -1191200400000, :clock? :standard}]
       {:floating-day "Sat>=1",
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1191200400000, :clock? :standard}
        {:utc -1112486400000, :clock? :standard}]
       {:day 2,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1127696400000, :clock? :standard}
        {:utc -1112486400000, :clock? :standard}]
       {:day 7,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1096851600000, :clock? :standard}
        {:utc -1112486400000, :clock? :standard}]
       {:day 30,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1063587600000, :clock? :standard}
        {:utc -1112486400000, :clock? :standard}]
       {:day 18,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1033347600000, :clock? :standard}
        {:utc -1112486400000, :clock? :standard}]
       {:day 3,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1112486400000, :clock? :standard}
        {:utc -1002502800000, :clock? :standard}]
       {:floating-day "Sat>=1",
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1002502800000, :clock? :standard}
        {:utc -950490000000, :clock? :standard}]
       {:day 26,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -969238800000, :clock? :standard}
        {:utc -950490000000, :clock? :standard}]
       {:day 15,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -950490000000, :clock? :standard}
        {:utc -942022800000, :clock? :standard}]
       {:day 18,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 11,
        :save 0}]
      [[{:utc -942022800000, :clock? :standard}
        {:utc -922669200000, :clock? :standard}]
       {:day 24,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 2,
        :save 3600000}]
      [[{:utc -922669200000, :clock? :standard}
        {:utc -906944400000, :clock? :standard}]
       {:day 5,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -906944400000, :clock? :standard}
        {:utc -858038400000, :clock? :standard}]
       {:day 5,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -873684000000, :clock? :standard}
        {:utc -858038400000, :clock? :standard}]
       {:day 25,
        :time {:hour 22, :minute 0, :time-suffix "s"},
        :month 4,
        :save 7200000}]
      [[{:utc -864007200000, :clock? :standard}
        {:utc -858038400000, :clock? :standard}]
       {:day 15,
        :time {:hour 22, :minute 0, :time-suffix "s"},
        :month 8,
        :save 3600000}]
      [[{:utc -842839200000, :clock? :standard}
        {:utc -858038400000, :clock? :standard}]
       {:day 17,
        :time {:hour 22, :minute 0, :time-suffix "s"},
        :month 4,
        :save 7200000}]
      [[{:utc -877910400000, :clock? :standard}
        {:utc -858038400000, :clock? :standard}]
       {:floating-day "Sat>=8",
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -858038400000, :clock? :standard}
        {:utc -831686400000, :clock? :standard}]
       {:floating-day "Sat>=24",
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -831686400000, :clock? :standard}
        {:utc -733795200000, :clock? :standard}]
       {:floating-day "Sat>=25",
        :time {:hour 22, :minute 0, :time-suffix "s"},
        :month 8,
        :save 3600000}]
      [[{:utc -810950400000, :clock? :standard}
        {:utc -733795200000, :clock? :standard}]
       {:floating-day "Sat>=21",
        :time {:hour 22, :minute 0, :time-suffix "s"},
        :month 4,
        :save 7200000}]
      [[{:utc -749606400000, :clock? :standard}
        {:utc -733795200000, :clock? :standard}]
       {:floating-day "Sat>=1",
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -733795200000, :clock? :standard}
        {:utc -718070400000, :clock? :standard}]
       {:floating-day "Sat>=1",
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -718070400000, :clock? :standard}
        {:utc -702259200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -702259200000, :clock? :standard}
        {:utc -591840000000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -591840000000, :clock? :standard}
        {:utc -576028800000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -576028800000, :clock? :standard}
        {:utc 228268800000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 228268800000, :clock? :standard}
        {:utc 243993600000, :clock? :standard}]
       {:day 27,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 243993600000, :clock? :standard}
        {:utc 260236800000, :clock? :standard}]
       {:day 25,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 276048000000, :clock? :standard}
        {:utc 260236800000, :clock? :standard}]
       {:day 1,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 260236800000, :clock? :standard}
        {:utc 307497600000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 323222400000, :clock? :standard}
        {:utc 307497600000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 307497600000, :clock? :standard}
        {:utc 354672000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]]},
    "Armenia"
    {:current nil,
     :history
     [[[{:utc 1301184000000, :clock? :standard}
        {:utc 1319932800000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]]},
    "WS"
    {:current
     {:standard
      {:from 1333238400000,
       :clock? :utc,
       :floating-day "Sun>=1",
       :time {:hour 4, :minute 0},
       :month 4,
       :save 0},
      :daylight-savings
      {:from 1348963200000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 3, :minute 0},
       :month 9,
       :save 1}},
     :history
     [[[{:utc 1285459200000, :clock? :utc}
        {:utc 1301616000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 1}]
      [[{:utc 1301616000000, :clock? :utc}
        {:utc 1316822400000, :clock? :utc}]
       {:floating-day "Sat>=1",
        :time {:hour 4, :minute 0},
        :month 4,
        :save 0}]
      [[{:utc 1316822400000, :clock? :utc}
        {:utc 1333238400000, :clock? :utc}]
       {:floating-day "lastSat",
        :time {:hour 3, :minute 0},
        :month 9,
        :save 1}]]},
    "Norway"
    {:current nil,
     :history
     [[[{:utc -1691881200000, :clock? :utc}
        {:utc -1680566400000, :clock? :utc}]
       {:day 22, :time {:hour 1, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1680566400000, :clock? :utc}
        {:utc -781048800000, :clock? :standard}]
       {:day 30, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -781048800000, :clock? :standard}
        {:utc -765324000000, :clock? :standard}]
       {:day 2,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -765324000000, :clock? :standard}
        {:utc -340848000000, :clock? :standard}]
       {:day 1,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -340848000000, :clock? :standard}
        {:utc -324950400000, :clock? :standard}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -324950400000, :clock? :standard}
        {:utc -147909600000, :clock? :standard}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]]},
    "Algeria"
    {:current nil,
     :history
     [[[{:utc -1689814800000, :clock? :standard}
        {:utc -1680480000000, :clock? :standard}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 6,
        :save 3600000}]
      [[{:utc -1665363600000, :clock? :standard}
        {:utc -1680480000000, :clock? :standard}]
       {:day 24,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1635123600000, :clock? :standard}
        {:utc -1680480000000, :clock? :standard}]
       {:day 9,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1680480000000, :clock? :standard}
        {:utc -1604278800000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1604278800000, :clock? :standard}
        {:utc -1552266000000, :clock? :standard}]
       {:day 1,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1574038800000, :clock? :standard}
        {:utc -1552266000000, :clock? :standard}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 2,
        :save 3600000}]
      [[{:utc -1552266000000, :clock? :standard}
        {:utc -1539997200000, :clock? :standard}]
       {:day 23,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1539997200000, :clock? :standard}
        {:utc -1531443600000, :clock? :standard}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1531443600000, :clock? :standard}
        {:utc -956365200000, :clock? :standard}]
       {:day 21,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 6,
        :save 0}]
      [[{:utc -956365200000, :clock? :standard}
        {:utc -950482800000, :clock? :utc}]
       {:day 11,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 9,
        :save 3600000}]
      [[{:utc -950482800000, :clock? :utc}
        {:utc -812678400000, :clock? :utc}]
       {:day 19, :time {:hour 1, :minute 0}, :month 11, :save 0}]
      [[{:utc -796255200000, :clock? :utc}
        {:utc -812678400000, :clock? :utc}]
       {:day 8, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -812678400000, :clock? :utc}
        {:utc -766620000000, :clock? :utc}]
       {:floating-day "Mon>=1",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -766620000000, :clock? :utc}
        {:utc 41468400000, :clock? :standard}]
       {:day 16, :time {:hour 1, :minute 0}, :month 9, :save 0}]
      [[{:utc 41468400000, :clock? :standard}
        {:utc 54774000000, :clock? :standard}]
       {:day 25,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 54774000000, :clock? :standard}
        {:utc 231724800000, :clock? :utc}]
       {:day 26,
        :time {:hour 23, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 231724800000, :clock? :utc}
        {:utc 246240000000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 246240000000, :clock? :utc}
        {:utc 259549200000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 259549200000, :clock? :utc}
        {:utc 275281200000, :clock? :utc}]
       {:day 24, :time {:hour 1, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 275281200000, :clock? :utc}
        {:utc 325468800000, :clock? :utc}]
       {:day 22, :time {:hour 3, :minute 0}, :month 9, :save 0}]
      [[{:utc 325468800000, :clock? :utc}
        {:utc 341805600000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]]},
    "Czech"
    {:current nil,
     :history
     [[[{:utc -781142400000, :clock? :standard}
        {:utc -765324000000, :clock? :standard}]
       {:floating-day "Mon>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -765324000000, :clock? :standard}
        {:utc -746575200000, :clock? :standard}]
       {:day 1,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -746575200000, :clock? :standard}
        {:utc -733795200000, :clock? :standard}]
       {:day 6,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc -716860800000, :clock? :standard}
        {:utc -733795200000, :clock? :standard}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -733795200000, :clock? :standard}
        {:utc -654213600000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]]},
    "Lux"
    {:current nil,
     :history
     [[[{:utc -1692493200000, :clock? :utc}
        {:utc -1680476400000, :clock? :utc}]
       {:day 14, :time {:hour 23, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1680476400000, :clock? :utc}
        {:utc -1662339600000, :clock? :utc}]
       {:day 1, :time {:hour 1, :minute 0}, :month 10, :save 0}]
      [[{:utc -1662339600000, :clock? :utc}
        {:utc -1650150000000, :clock? :utc}]
       {:day 28, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1650150000000, :clock? :utc}
        {:utc -1632009600000, :clock? :standard}]
       {:day 17, :time {:hour 1, :minute 0}, :month 9, :save 0}]
      [[{:utc -1632009600000, :clock? :standard}
        {:utc -1618790400000, :clock? :standard}]
       {:floating-day "Mon>=15",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1618790400000, :clock? :standard}
        {:utc -1604278800000, :clock? :utc}]
       {:floating-day "Mon>=15",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -1604278800000, :clock? :utc}
        {:utc -1585515600000, :clock? :utc}]
       {:day 1, :time {:hour 23, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -1585515600000, :clock? :utc}
        {:utc -1574038800000, :clock? :utc}]
       {:day 5, :time {:hour 3, :minute 0}, :month 10, :save 0}]
      [[{:utc -1574038800000, :clock? :utc}
        {:utc -1552255200000, :clock? :utc}]
       {:day 14, :time {:hour 23, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -1552255200000, :clock? :utc}
        {:utc -1539997200000, :clock? :utc}]
       {:day 24, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -1539997200000, :clock? :utc}
        {:utc -1520546400000, :clock? :utc}]
       {:day 14, :time {:hour 23, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -1520546400000, :clock? :utc}
        {:utc -1507510800000, :clock? :utc}]
       {:day 26, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -1507510800000, :clock? :utc}
        {:utc -1491091200000, :clock? :utc}]
       {:day 25, :time {:hour 23, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -1491091200000, :clock? :utc}
        {:utc -1473642000000, :clock? :utc}]
       {:floating-day "Sun>=2",
        :time {:hour 1, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -1473642000000, :clock? :utc}
        {:utc -1459555200000, :clock? :utc}]
       {:day 21, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1459555200000, :clock? :utc}
        {:utc -1444006800000, :clock? :utc}]
       {:floating-day "Sun>=2",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -1444006800000, :clock? :utc}
        {:utc -1427932800000, :clock? :utc}]
       {:day 29, :time {:hour 23, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -1411866000000, :clock? :utc}
        {:utc -1427932800000, :clock? :utc}]
       {:day 5, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1379293200000, :clock? :utc}
        {:utc -1427932800000, :clock? :utc}]
       {:day 17, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1348448400000, :clock? :utc}
        {:utc -1427932800000, :clock? :utc}]
       {:day 9, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1427932800000, :clock? :utc}
        {:utc -1316394000000, :clock? :utc}]
       {:floating-day "Sun>=2",
        :time {:hour 1, :minute 0},
        :month 10,
        :save 0}]]},
    "Taiwan"
    {:current nil,
     :history
     [[[{:utc -745804800000, :clock? :utc}
        {:utc -733795200000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -733795200000, :clock? :utc}
        {:utc -716860800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -716860800000, :clock? :utc}
        {:utc -699580800000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -699580800000, :clock? :utc}
        {:utc -683856000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc -683856000000, :clock? :utc}
        {:utc -670636800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -670636800000, :clock? :utc}
        {:utc -562896000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -562896000000, :clock? :utc}
        {:utc -541728000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -541728000000, :clock? :utc}
        {:utc -528681600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc -528681600000, :clock? :utc}
        {:utc -449798400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -449798400000, :clock? :utc}
        {:utc -302486400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -302486400000, :clock? :utc}
        {:utc 149817600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 134006400000, :clock? :utc}
        {:utc 149817600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 149817600000, :clock? :utc}
        {:utc 299635200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 299635200000, :clock? :utc}
        {:utc 307584000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 7, :save 3600000}]]},
    "Albania"
    {:current nil,
     :history
     [[[{:utc -932342400000, :clock? :utc}
        {:utc -857250000000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -857250000000, :clock? :utc}
        {:utc -844556400000, :clock? :utc}]
       {:day 2, :time {:hour 3, :minute 0}, :month 11, :save 0}]
      [[{:utc -844556400000, :clock? :utc}
        {:utc -843512400000, :clock? :utc}]
       {:day 29, :time {:hour 2, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -843512400000, :clock? :utc}
        {:utc 136857600000, :clock? :utc}]
       {:day 10, :time {:hour 3, :minute 0}, :month 4, :save 0}]
      [[{:utc 136857600000, :clock? :utc}
        {:utc 149904000000, :clock? :utc}]
       {:day 4, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 149904000000, :clock? :utc}
        {:utc 168134400000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 168134400000, :clock? :utc}
        {:utc 181440000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 181440000000, :clock? :utc}
        {:utc 199843200000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 199843200000, :clock? :utc}
        {:utc 213148800000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 213148800000, :clock? :utc}
        {:utc 231897600000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 231897600000, :clock? :utc}
        {:utc 244598400000, :clock? :utc}]
       {:day 8, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 244598400000, :clock? :utc}
        {:utc 263260800000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 263260800000, :clock? :utc}
        {:utc 276048000000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 276048000000, :clock? :utc}
        {:utc 294710400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 294710400000, :clock? :utc}
        {:utc 307497600000, :clock? :utc}]
       {:day 5, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 307497600000, :clock? :utc}
        {:utc 326160000000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 326160000000, :clock? :utc}
        {:utc 339465600000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 339465600000, :clock? :utc}
        {:utc 357091200000, :clock? :utc}]
       {:day 4, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 357091200000, :clock? :utc}
        {:utc 370396800000, :clock? :utc}]
       {:day 26, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 370396800000, :clock? :utc}
        {:utc 389145600000, :clock? :utc}]
       {:day 27, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 389145600000, :clock? :utc}
        {:utc 402451200000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 402451200000, :clock? :utc}
        {:utc 419472000000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 419472000000, :clock? :utc}
        {:utc 433814400000, :clock? :utc}]
       {:day 18, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 433814400000, :clock? :utc}
        {:utc 449625600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]]},
    "Chatham"
    {:current
     {:daylight-savings
      {:from 1191110400000,
       :clock? :standard,
       :floating-day "lastSun",
       :time {:hour 2, :minute 45, :time-suffix "s"},
       :month 9,
       :save 3600000},
      :standard
      {:from 1207008000000,
       :clock? :standard,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 45, :time-suffix "s"},
       :month 4,
       :save 0}},
     :history
     [[[{:utc 152496000000, :clock? :standard}
        {:utc 162345600000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 45, :time-suffix "s"},
        :month 11,
        :save 3600000}]
      [[{:utc 162345600000, :clock? :standard}
        {:utc 183513600000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 45, :time-suffix "s"},
        :month 2,
        :save 0}]
      [[{:utc 183513600000, :clock? :standard}
        {:utc 194486400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 45, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 194486400000, :clock? :standard}
        {:utc 623808000000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 45, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 623808000000, :clock? :standard}
        {:utc 637459200000, :clock? :standard}]
       {:floating-day "Sun>=8",
        :time {:hour 2, :minute 45, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 654739200000, :clock? :standard}
        {:utc 637459200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 45, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 637459200000, :clock? :standard}
        {:utc 1191110400000, :clock? :standard}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 45, :time-suffix "s"},
        :month 3,
        :save 0}]]},
    "CA"
    {:current nil,
     :history
     [[[{:utc -687995940000, :clock? :utc}
        {:utc -662680800000, :clock? :utc}]
       {:day 14, :time {:hour 2, :minute 1}, :month 3, :save 3600000}]
      [[{:utc -662680800000, :clock? :utc}
        {:utc -620870400000, :clock? :utc}]
       {:day 1, :time {:hour 2, :minute 0}, :month 1, :save 0}]
      [[{:utc -608169600000, :clock? :utc}
        {:utc -620870400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -620870400000, :clock? :utc}
        {:utc -226540800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Cyprus"
    {:current nil,
     :history
     [[[{:utc 166579200000, :clock? :utc}
        {:utc 182304000000, :clock? :utc}]
       {:day 13, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 182304000000, :clock? :utc}
        {:utc 200966400000, :clock? :utc}]
       {:day 12, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 200966400000, :clock? :utc}
        {:utc 213840000000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 213840000000, :clock? :utc}
        {:utc 228700800000, :clock? :utc}]
       {:day 11, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 243993600000, :clock? :utc}
        {:utc 228700800000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 276134400000, :clock? :utc}
        {:utc 228700800000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 228700800000, :clock? :utc}
        {:utc 307497600000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 307497600000, :clock? :utc}
        {:utc 354672000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]]},
    "AV"
    {:current
     {:standard
      {:from 1207008000000,
       :clock? :standard,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0, :time-suffix "s"},
       :month 4,
       :save 0},
      :daylight-savings
      {:from 1222819200000,
       :clock? :standard,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0, :time-suffix "s"},
       :month 10,
       :save 3600000}},
     :history
     [[[{:utc 67996800000, :clock? :standard}
        {:utc 57715200000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 2,
        :save 0}]
      [[{:utc 57715200000, :clock? :standard}
        {:utc 99792000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 99792000000, :clock? :standard}
        {:utc 529718400000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 529718400000, :clock? :standard}
        {:utc 511228800000, :clock? :standard}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 511228800000, :clock? :standard}
        {:utc 594172800000, :clock? :standard}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 667785600000, :clock? :standard}
        {:utc 594172800000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 594172800000, :clock? :standard}
        {:utc 796176000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 967334400000, :clock? :standard}
        {:utc 796176000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 8,
        :save 3600000}]
      [[{:utc 796176000000, :clock? :standard}
        {:utc 1004227200000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 1143849600000, :clock? :standard}
        {:utc 1004227200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 0}]
      [[{:utc 1004227200000, :clock? :standard}
        {:utc 1174780800000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 1174780800000, :clock? :standard}
        {:utc 1222819200000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]]},
    "Dhaka"
    {:current nil,
     :history
     [[[{:utc 1245452400000, :clock? :utc}
        {:utc 1262304000000, :clock? :utc}]
       {:day 19,
        :time {:hour 23, :minute 0},
        :month 6,
        :save 3600000}]]},
    "Pike"
    {:current nil,
     :history
     [[[{:utc -463017600000, :clock? :utc}
        {:utc -450316800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -450316800000, :clock? :utc}
        {:utc -431568000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -431568000000, :clock? :utc}
        {:utc -257990400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Finland"
    {:current nil,
     :history
     [[[{:utc -875664000000, :clock? :utc}
        {:utc -859762800000, :clock? :utc}]
       {:day 2, :time {:hour 24, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -859762800000, :clock? :utc}
        {:utc 354672000000, :clock? :utc}]
       {:day 4, :time {:hour 1, :minute 0}, :month 10, :save 0}]
      [[{:utc 354672000000, :clock? :utc}
        {:utc 370396800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 3600000}]]},
    "Para"
    {:current
     {:daylight-savings
      {:from 1285891200000,
       :clock? :utc,
       :floating-day "Sun>=1",
       :time {:hour 0, :minute 0},
       :month 10,
       :save 3600000},
      :standard
      {:from 1363910400000,
       :clock? :utc,
       :floating-day "Sun>=22",
       :time {:hour 0, :minute 0},
       :month 3,
       :save 0}},
     :history
     [[[{:utc 162864000000, :clock? :utc}
        {:utc 181353600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc 181353600000, :clock? :utc}
        {:utc 291772800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 625017600000, :clock? :utc}
        {:utc 291772800000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 654739200000, :clock? :utc}
        {:utc 291772800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 291772800000, :clock? :utc}
        {:utc 686707200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 0}]
      [[{:utc 686707200000, :clock? :utc}
        {:utc 699408000000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 699408000000, :clock? :utc}
        {:utc 718243200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc 718243200000, :clock? :utc}
        {:utc 733536000000, :clock? :utc}]
       {:day 5, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 733536000000, :clock? :utc}
        {:utc 749433600000, :clock? :utc}]
       {:day 31, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc 749433600000, :clock? :utc}
        {:utc 762307200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 762307200000, :clock? :utc}
        {:utc 844128000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 825638400000, :clock? :utc}
        {:utc 844128000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc 856656000000, :clock? :utc}
        {:utc 844128000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 2,
        :save 0}]
      [[{:utc 844128000000, :clock? :utc}
        {:utc 888710400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 3600000}]
      [[{:utc 888710400000, :clock? :utc}
        {:utc 1030838400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc 1030838400000, :clock? :utc}
        {:utc 1017619200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 3600000}]
      [[{:utc 1017619200000, :clock? :utc}
        {:utc 1097798400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 0}]
      [[{:utc 1097798400000, :clock? :utc}
        {:utc 1110240000000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 3600000}]
      [[{:utc 1110240000000, :clock? :utc}
        {:utc 1285891200000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc 1270684800000, :clock? :utc}
        {:utc 1285891200000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 0}]]},
    "GB-Eire"
    {:current nil,
     :history
     [[[{:utc -1691964000000, :clock? :standard}
        {:utc -1680472800000, :clock? :standard}]
       {:day 21,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc -1680472800000, :clock? :standard}
        {:utc -1664143200000, :clock? :standard}]
       {:day 1,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1664143200000, :clock? :standard}
        {:utc -1650146400000, :clock? :standard}]
       {:day 8,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1650146400000, :clock? :standard}
        {:utc -1633903200000, :clock? :standard}]
       {:day 17,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -1633903200000, :clock? :standard}
        {:utc -1617487200000, :clock? :standard}]
       {:day 24,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1617487200000, :clock? :standard}
        {:utc -1601848800000, :clock? :standard}]
       {:day 30,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -1601848800000, :clock? :standard}
        {:utc -1586037600000, :clock? :standard}]
       {:day 30,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1586037600000, :clock? :standard}
        {:utc -1570399200000, :clock? :standard}]
       {:day 29,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -1570399200000, :clock? :standard}
        {:utc -1552168800000, :clock? :standard}]
       {:day 28,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1552168800000, :clock? :standard}
        {:utc -1538344800000, :clock? :standard}]
       {:day 25,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1538344800000, :clock? :standard}
        {:utc -1522533600000, :clock? :standard}]
       {:day 3,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1522533600000, :clock? :standard}
        {:utc -1507500000000, :clock? :standard}]
       {:day 3,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1507500000000, :clock? :standard}
        {:utc -1490565600000, :clock? :standard}]
       {:day 26,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1490565600000, :clock? :standard}
        {:utc -1474156800000, :clock? :standard}]
       {:day 8,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1474156800000, :clock? :standard}
        {:utc -1460937600000, :clock? :standard}]
       {:floating-day "Sun>=16",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1460937600000, :clock? :standard}
        {:utc -1443139200000, :clock? :standard}]
       {:floating-day "Sun>=16",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -1443139200000, :clock? :standard}
        {:utc -1396396800000, :clock? :standard}]
       {:floating-day "Sun>=9",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1410998400000, :clock? :standard}
        {:utc -1396396800000, :clock? :standard}]
       {:floating-day "Sun>=16",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1348531200000, :clock? :standard}
        {:utc -1396396800000, :clock? :standard}]
       {:floating-day "Sun>=9",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1316304000000, :clock? :standard}
        {:utc -1396396800000, :clock? :standard}]
       {:floating-day "Sun>=16",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1253836800000, :clock? :standard}
        {:utc -1396396800000, :clock? :standard}]
       {:floating-day "Sun>=9",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1221696000000, :clock? :standard}
        {:utc -1396396800000, :clock? :standard}]
       {:floating-day "Sun>=16",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1159142400000, :clock? :standard}
        {:utc -1396396800000, :clock? :standard}]
       {:floating-day "Sun>=9",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1127001600000, :clock? :standard}
        {:utc -1396396800000, :clock? :standard}]
       {:floating-day "Sun>=16",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1096070400000, :clock? :standard}
        {:utc -1396396800000, :clock? :standard}]
       {:floating-day "Sun>=9",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1063843200000, :clock? :standard}
        {:utc -1396396800000, :clock? :standard}]
       {:floating-day "Sun>=16",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1396396800000, :clock? :standard}
        {:utc -1001376000000, :clock? :standard}]
       {:floating-day "Sun>=2",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1001376000000, :clock? :standard}
        {:utc -950745600000, :clock? :standard}]
       {:floating-day "Sun>=9",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -969235200000, :clock? :standard}
        {:utc -950745600000, :clock? :standard}]
       {:floating-day "Sun>=16",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -950745600000, :clock? :standard}
        {:utc -942192000000, :clock? :standard}]
       {:floating-day "Sun>=16",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 11,
        :save 0}]
      [[{:utc -942192000000, :clock? :standard}
        {:utc -765244800000, :clock? :standard}]
       {:floating-day "Sun>=23",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 2,
        :save 3600000}]
      [[{:utc -904694400000, :clock? :standard}
        {:utc -765244800000, :clock? :standard}]
       {:floating-day "Sun>=2",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 5,
        :save 7200000}]
      [[{:utc -896140800000, :clock? :standard}
        {:utc -765244800000, :clock? :standard}]
       {:floating-day "Sun>=9",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 8,
        :save 3600000}]
      [[{:utc -875750400000, :clock? :standard}
        {:utc -765244800000, :clock? :standard}]
       {:floating-day "Sun>=2",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 4,
        :save 7200000}]
      [[{:utc -798163200000, :clock? :standard}
        {:utc -765244800000, :clock? :standard}]
       {:floating-day "Sun>=16",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 9,
        :save 3600000}]
      [[{:utc -781056000000, :clock? :standard}
        {:utc -765244800000, :clock? :standard}]
       {:floating-day "Mon>=2",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 4,
        :save 7200000}]
      [[{:utc -772588800000, :clock? :standard}
        {:utc -765244800000, :clock? :standard}]
       {:floating-day "Sun>=9",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 7,
        :save 3600000}]
      [[{:utc -765244800000, :clock? :standard}
        {:utc -748915200000, :clock? :standard}]
       {:floating-day "Sun>=2",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -748915200000, :clock? :standard}
        {:utc -699487200000, :clock? :standard}]
       {:floating-day "Sun>=9",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -719445600000, :clock? :standard}
        {:utc -699487200000, :clock? :standard}]
       {:day 16,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -717030000000, :clock? :standard}
        {:utc -699487200000, :clock? :standard}]
       {:day 13,
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 4,
        :save 7200000}]
      [[{:utc -706748400000, :clock? :standard}
        {:utc -699487200000, :clock? :standard}]
       {:day 10,
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 8,
        :save 3600000}]
      [[{:utc -699487200000, :clock? :standard}
        {:utc -687996000000, :clock? :standard}]
       {:day 2,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 11,
        :save 0}]
      [[{:utc -687996000000, :clock? :standard}
        {:utc -668037600000, :clock? :standard}]
       {:day 14,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -668037600000, :clock? :standard}
        {:utc -654732000000, :clock? :standard}]
       {:day 31,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -654732000000, :clock? :standard}
        {:utc -636588000000, :clock? :standard}]
       {:day 3,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -636588000000, :clock? :standard}
        {:utc -622252800000, :clock? :standard}]
       {:day 30,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -622252800000, :clock? :standard}
        {:utc -605836800000, :clock? :standard}]
       {:floating-day "Sun>=14",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -605836800000, :clock? :standard}
        {:utc -527385600000, :clock? :standard}]
       {:floating-day "Sun>=21",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -527385600000, :clock? :standard}
        {:utc -512784000000, :clock? :standard}]
       {:floating-day "Sun>=16",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -496454400000, :clock? :standard}
        {:utc -512784000000, :clock? :standard}]
       {:floating-day "Sun>=9",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -464313600000, :clock? :standard}
        {:utc -512784000000, :clock? :standard}]
       {:floating-day "Sun>=16",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -401760000000, :clock? :standard}
        {:utc -512784000000, :clock? :standard}]
       {:floating-day "Sun>=9",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -369619200000, :clock? :standard}
        {:utc -512784000000, :clock? :standard}]
       {:floating-day "Sun>=16",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -512784000000, :clock? :standard}
        {:utc -307065600000, :clock? :standard}]
       {:floating-day "Sun>=2",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -307065600000, :clock? :standard}
        {:utc -258508800000, :clock? :standard}]
       {:floating-day "Sun>=9",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -276739200000, :clock? :standard}
        {:utc -258508800000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -182649600000, :clock? :standard}
        {:utc -258508800000, :clock? :standard}]
       {:floating-day "Sun>=19",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -258508800000, :clock? :standard}
        {:utc -59004000000, :clock? :standard}]
       {:floating-day "Sun>=23",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -59004000000, :clock? :standard}
        {:utc 88646400000, :clock? :standard}]
       {:day 18,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 2,
        :save 3600000}]
      [[{:utc 69552000000, :clock? :standard}
        {:utc 88646400000, :clock? :standard}]
       {:floating-day "Sun>=16",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 88646400000, :clock? :standard}
        {:utc 354672000000, :clock? :utc}]
       {:floating-day "Sun>=23",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 372643200000, :clock? :utc}
        {:utc 354672000000, :clock? :utc}]
       {:floating-day "Sun>=23",
        :time {:hour 1, :minute 0, :time-suffix "u"},
        :month 10,
        :save 0}]
      [[{:utc 354672000000, :clock? :utc}
        {:utc 656553600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "u"},
        :month 3,
        :save 3600000}]]},
    "Kyrgyz"
    {:current nil,
     :history
     [[[{:utc 702604800000, :clock? :standard}
        {:utc 717552000000, :clock? :utc}]
       {:floating-day "Sun>=7",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 717552000000, :clock? :utc}
        {:utc 859680000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 877824000000, :clock? :utc}
        {:utc 859680000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 30},
        :month 10,
        :save 0}]]},
    "Denmark"
    {:current nil,
     :history
     [[[{:utc -1692493200000, :clock? :utc}
        {:utc -1680483600000, :clock? :utc}]
       {:day 14, :time {:hour 23, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1680483600000, :clock? :utc}
        {:utc -935107200000, :clock? :utc}]
       {:day 30, :time {:hour 23, :minute 0}, :month 9, :save 0}]
      [[{:utc -935107200000, :clock? :utc}
        {:utc -769384800000, :clock? :standard}]
       {:day 15, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -781048800000, :clock? :standard}
        {:utc -769384800000, :clock? :standard}]
       {:day 2,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -769384800000, :clock? :standard}
        {:utc -747007200000, :clock? :standard}]
       {:day 15,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 8,
        :save 0}]
      [[{:utc -747007200000, :clock? :standard}
        {:utc -736380000000, :clock? :standard}]
       {:day 1,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc -736380000000, :clock? :standard}
        {:utc -715212000000, :clock? :standard}]
       {:day 1,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -715212000000, :clock? :standard}
        {:utc -706744800000, :clock? :standard}]
       {:day 4,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc -706744800000, :clock? :standard}
        {:utc -683157600000, :clock? :standard}]
       {:day 10,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 8,
        :save 0}]
      [[{:utc -683157600000, :clock? :standard}
        {:utc -675295200000, :clock? :standard}]
       {:day 9,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]]},
    "AS"
    {:current
     {:standard
      {:from 1207008000000,
       :clock? :standard,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0, :time-suffix "s"},
       :month 4,
       :save 0},
      :daylight-savings
      {:from 1222819200000,
       :clock? :standard,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0, :time-suffix "s"},
       :month 10,
       :save 3600000}},
     :history
     [[[{:utc 68004000000, :clock? :standard}
        {:utc 57715200000, :clock? :standard}]
       {:day 27,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 2,
        :save 0}]
      [[{:utc 57715200000, :clock? :standard}
        {:utc 99792000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 99792000000, :clock? :standard}
        {:utc 530071200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 530071200000, :clock? :standard}
        {:utc 511228800000, :clock? :standard}]
       {:day 19,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 511228800000, :clock? :standard}
        {:utc 562118400000, :clock? :standard}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 667965600000, :clock? :standard}
        {:utc 562118400000, :clock? :standard}]
       {:day 3,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 701229600000, :clock? :standard}
        {:utc 562118400000, :clock? :standard}]
       {:day 22,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 731469600000, :clock? :standard}
        {:utc 562118400000, :clock? :standard}]
       {:day 7,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 764128800000, :clock? :standard}
        {:utc 562118400000, :clock? :standard}]
       {:day 20,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 796176000000, :clock? :standard}
        {:utc 562118400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]
      [[{:utc 1143943200000, :clock? :standard}
        {:utc 562118400000, :clock? :standard}]
       {:day 2,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 0}]
      [[{:utc 562118400000, :clock? :standard}
        {:utc 1174780800000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 1174780800000, :clock? :standard}
        {:utc 1222819200000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 0}]]},
    "Peru"
    {:current nil,
     :history
     [[[{:utc -1009843200000, :clock? :utc}
        {:utc -1002067200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 1, :save 3600000}]
      [[{:utc -1002067200000, :clock? :utc}
        {:utc -986774400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 0}]
      [[{:utc -986774400000, :clock? :utc}
        {:utc -971222400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 3600000}]
      [[{:utc -971222400000, :clock? :utc}
        {:utc 504921600000, :clock? :utc}]
       {:floating-day "Sun>=24",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc 504921600000, :clock? :utc}
        {:utc 512697600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 1, :save 3600000}]
      [[{:utc 512697600000, :clock? :utc}
        {:utc 631152000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 0}]
      [[{:utc 631152000000, :clock? :utc}
        {:utc 638928000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 1, :save 3600000}]
      [[{:utc 638928000000, :clock? :utc}
        {:utc 757382400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 0}]
      [[{:utc 757382400000, :clock? :utc}
        {:utc 765158400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 1, :save 3600000}]]},
    "RussiaAsia"
    {:current nil,
     :history
     [[[{:utc 370742400000, :clock? :utc}
        {:utc 354931200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 354931200000, :clock? :utc}
        {:utc 465350400000, :clock? :standard}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 465350400000, :clock? :standard}
        {:utc 481075200000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 481075200000, :clock? :standard}
        {:utc 846374400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]]},
    "Neth"
    {:current nil,
     :history
     [[[{:utc -1693699200000, :clock? :utc}
        {:utc -1680480000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1680480000000, :clock? :utc}
        {:utc -1663452000000, :clock? :standard}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -1663452000000, :clock? :standard}
        {:utc -1650146400000, :clock? :standard}]
       {:day 16,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1650146400000, :clock? :standard}
        {:utc -1633219200000, :clock? :standard}]
       {:day 17,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -1633219200000, :clock? :standard}
        {:utc -1617494400000, :clock? :standard}]
       {:floating-day "Mon>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1617494400000, :clock? :standard}
        {:utc -1507507200000, :clock? :standard}]
       {:floating-day "lastMon",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -1507507200000, :clock? :standard}
        {:utc -1491091200000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1470182400000, :clock? :standard}
        {:utc -1491091200000, :clock? :standard}]
       {:floating-day "Fri>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 6,
        :save 3600000}]
      [[{:utc -1444003200000, :clock? :standard}
        {:utc -1491091200000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -1407024000000, :clock? :standard}
        {:utc -1491091200000, :clock? :standard}]
       {:floating-day "Fri>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 6,
        :save 3600000}]
      [[{:utc -1376949600000, :clock? :standard}
        {:utc -1491091200000, :clock? :standard}]
       {:day 15,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc -1186956000000, :clock? :standard}
        {:utc -1491091200000, :clock? :standard}]
       {:day 22,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc -1491091200000, :clock? :standard}
        {:utc -1156024800000, :clock? :standard}]
       {:floating-day "Sun>=2",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1156024800000, :clock? :standard}
        {:utc -1017705600000, :clock? :standard}]
       {:day 15,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc -1029189600000, :clock? :standard}
        {:utc -1017705600000, :clock? :standard}]
       {:day 22,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc -1025740800000, :clock? :utc}
        {:utc -1017705600000, :clock? :standard}]
       {:day 1, :time {:hour 0, :minute 0}, :month 7, :save 3600000}]
      [[{:utc -1017705600000, :clock? :standard}
        {:utc -998258400000, :clock? :standard}]
       {:floating-day "Sun>=2",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -998258400000, :clock? :standard}
        {:utc -766620000000, :clock? :standard}]
       {:day 15,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc -781048800000, :clock? :standard}
        {:utc -766620000000, :clock? :standard}]
       {:day 2,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]]},
    "Iceland"
    {:current nil,
     :history
     [[[{:utc -1647212400000, :clock? :utc}
        {:utc -1668214800000, :clock? :utc}]
       {:day 21, :time {:hour 1, :minute 0}, :month 10, :save 0}]
      [[{:utc -1668214800000, :clock? :utc}
        {:utc -1613430000000, :clock? :utc}]
       {:day 19, :time {:hour 23, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -1613430000000, :clock? :utc}
        {:utc -1539565200000, :clock? :utc}]
       {:day 16, :time {:hour 1, :minute 0}, :month 11, :save 0}]
      [[{:utc -1539565200000, :clock? :utc}
        {:utc -1531350000000, :clock? :utc}]
       {:day 19, :time {:hour 23, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -1531350000000, :clock? :utc}
        {:utc -968029200000, :clock? :utc}]
       {:day 23, :time {:hour 1, :minute 0}, :month 6, :save 0}]
      [[{:utc -968029200000, :clock? :utc}
        {:utc -952293600000, :clock? :utc}]
       {:day 29, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -952293600000, :clock? :utc}
        {:utc -942012000000, :clock? :utc}]
       {:day 29, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -942012000000, :clock? :utc}
        {:utc -920332800000, :clock? :standard}]
       {:day 25, :time {:hour 2, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -920332800000, :clock? :standard}
        {:utc -909964800000, :clock? :standard}]
       {:floating-day "Sun>=2",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 11,
        :save 0}]
      [[{:utc -909964800000, :clock? :standard}
        {:utc -858211200000, :clock? :standard}]
       {:floating-day "Sun>=2",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -846979200000, :clock? :standard}
        {:utc -858211200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -858211200000, :clock? :standard}
        {:utc -718070400000, :clock? :standard}]
       {:floating-day "Sun>=22",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -636591600000, :clock? :standard}
        {:utc -718070400000, :clock? :standard}]
       {:day 30,
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -605750400000, :clock? :standard}
        {:utc -718070400000, :clock? :standard}]
       {:floating-day "Sun>=22",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -718070400000, :clock? :standard}
        {:utc -68684400000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]]},
    "NBorneo"
    {:current nil,
     :history
     [[[{:utc -1082419200000, :clock? :utc}
        {:utc -1074556800000, :clock? :utc}]
       {:day 14, :time {:hour 0, :minute 0}, :month 9, :save 1200000}]]},
    "Falk"
    {:current nil,
     :history
     [[[{:utc -1018224000000, :clock? :utc}
        {:utc -1003190400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 3600000}]
      [[{:utc -954720000000, :clock? :utc}
        {:utc -1003190400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -1003190400000, :clock? :utc}
        {:utc -923270400000, :clock? :utc}]
       {:floating-day "Sun>=19",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc -923270400000, :clock? :utc}
        {:utc -852076800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 3600000}]
      [[{:utc -852076800000, :clock? :utc}
        {:utc 433296000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 1, :save 0}]
      [[{:utc 433296000000, :clock? :utc}
        {:utc 452044800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 3600000}]
      [[{:utc 464140800000, :clock? :utc}
        {:utc 452044800000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 9, :save 3600000}]
      [[{:utc 452044800000, :clock? :utc}
        {:utc 495072000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 0}]
      [[{:utc 495072000000, :clock? :utc}
        {:utc 513993600000, :clock? :utc}]
       {:floating-day "Sun>=9",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 3600000}]
      [[{:utc 513993600000, :clock? :utc}
        {:utc 999302400000, :clock? :utc}]
       {:floating-day "Sun>=16",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 0}]
      [[{:utc 987292800000, :clock? :utc}
        {:utc 999302400000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 0}]]},
    "Spain"
    {:current nil,
     :history
     [[[{:utc -1631926800000, :clock? :utc}
        {:utc -1616889600000, :clock? :standard}]
       {:day 15, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1616889600000, :clock? :standard}
        {:utc -1601168400000, :clock? :utc}]
       {:day 6,
        :time {:hour 24, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1601168400000, :clock? :utc}
        {:utc -1427673600000, :clock? :standard}]
       {:day 6, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1442451600000, :clock? :utc}
        {:utc -1427673600000, :clock? :standard}]
       {:day 16, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1427673600000, :clock? :standard}
        {:utc -1379293200000, :clock? :utc}]
       {:day 4,
        :time {:hour 24, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1379293200000, :clock? :utc}
        {:utc -1364947200000, :clock? :standard}]
       {:day 17, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1348448400000, :clock? :utc}
        {:utc -1364947200000, :clock? :standard}]
       {:day 9, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1316390400000, :clock? :utc}
        {:utc -1364947200000, :clock? :standard}]
       {:day 15, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1364947200000, :clock? :standard}
        {:utc -1284339600000, :clock? :utc}]
       {:floating-day "Sat>=1",
        :time {:hour 24, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1284339600000, :clock? :utc}
        {:utc -1017619200000, :clock? :standard}]
       {:day 20, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1026954000000, :clock? :utc}
        {:utc -1017619200000, :clock? :standard}]
       {:day 16, :time {:hour 23, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -1017619200000, :clock? :standard}
        {:utc -1001898000000, :clock? :utc}]
       {:day 2,
        :time {:hour 24, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -1001898000000, :clock? :utc}
        {:utc -954115200000, :clock? :standard}]
       {:day 2, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -999478800000, :clock? :utc}
        {:utc -954115200000, :clock? :standard}]
       {:day 30, :time {:hour 23, :minute 0}, :month 4, :save 7200000}]
      [[{:utc -986083200000, :clock? :utc}
        {:utc -954115200000, :clock? :standard}]
       {:day 2, :time {:hour 24, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -954115200000, :clock? :standard}
        {:utc -873075600000, :clock? :utc}]
       {:day 7,
        :time {:hour 24, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -873075600000, :clock? :utc}
        {:utc -862614000000, :clock? :utc}]
       {:day 2, :time {:hour 23, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -862614000000, :clock? :utc}
        {:utc -843264000000, :clock? :utc}]
       {:day 1, :time {:hour 1, :minute 0}, :month 9, :save 0}]
      [[{:utc -828489600000, :clock? :utc}
        {:utc -843264000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 1, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -843264000000, :clock? :utc}
        {:utc -765417600000, :clock? :utc}]
       {:floating-day "Sat>=13",
        :time {:hour 23, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -765417600000, :clock? :utc}
        {:utc -652323600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -652323600000, :clock? :utc}
        {:utc -639010800000, :clock? :utc}]
       {:day 30, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -639010800000, :clock? :utc}
        {:utc 134956800000, :clock? :utc}]
       {:day 2, :time {:hour 1, :minute 0}, :month 10, :save 0}]
      [[{:utc 134956800000, :clock? :utc}
        {:utc 149817600000, :clock? :utc}]
       {:floating-day "Sat>=12",
        :time {:hour 23, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 149817600000, :clock? :utc}
        {:utc 196815600000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 1, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 196815600000, :clock? :utc}
        {:utc 212544000000, :clock? :utc}]
       {:day 27, :time {:hour 23, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 212544000000, :clock? :utc}
        {:utc 228870000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 228870000000, :clock? :utc}
        {:utc 276055200000, :clock? :standard}]
       {:day 2, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 260330400000, :clock? :standard}
        {:utc 276055200000, :clock? :standard}]
       {:day 2,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]]},
    "Germany"
    {:current nil,
     :history
     [[[{:utc -748476000000, :clock? :standard}
        {:utc -733269600000, :clock? :standard}]
       {:day 14,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -733269600000, :clock? :standard}
        {:utc -717627600000, :clock? :standard}]
       {:day 7,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -717627600000, :clock? :standard}
        {:utc -702259200000, :clock? :standard}]
       {:day 6,
        :time {:hour 3, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -714607200000, :clock? :standard}
        {:utc -702259200000, :clock? :standard}]
       {:day 11,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 5,
        :save 7200000}]
      [[{:utc -710370000000, :clock? :utc}
        {:utc -702259200000, :clock? :standard}]
       {:day 29, :time {:hour 3, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -684972000000, :clock? :standard}
        {:utc -702259200000, :clock? :standard}]
       {:day 18,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -702259200000, :clock? :standard}
        {:utc -654127200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]]},
    "Azer"
    {:current nil,
     :history
     [[[{:utc 859680000000, :clock? :utc}
        {:utc 877824000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 4, :minute 0},
        :month 3,
        :save 3600000}]]},
    "Turkey"
    {:current nil,
     :history
     [[[{:utc -1693699200000, :clock? :utc}
        {:utc -1680480000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1680480000000, :clock? :utc}
        {:utc -1570406400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -1570406400000, :clock? :utc}
        {:utc -1552176000000, :clock? :utc}]
       {:day 28, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -1552176000000, :clock? :utc}
        {:utc -1538352000000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -1538352000000, :clock? :utc}
        {:utc -1522540800000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1522540800000, :clock? :utc}
        {:utc -1507507200000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -1507507200000, :clock? :utc}
        {:utc -1490572800000, :clock? :utc}]
       {:day 26, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -1490572800000, :clock? :utc}
        {:utc -1440201600000, :clock? :utc}]
       {:day 8, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -1440201600000, :clock? :utc}
        {:utc -1428019200000, :clock? :utc}]
       {:day 13, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1428019200000, :clock? :utc}
        {:utc -1409702400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -1409702400000, :clock? :utc}
        {:utc -922752000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -931132800000, :clock? :utc}
        {:utc -922752000000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -922752000000, :clock? :utc}
        {:utc -917827200000, :clock? :utc}]
       {:day 5, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -917827200000, :clock? :utc}
        {:utc -892425600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 12, :save 3600000}]
      [[{:utc -892425600000, :clock? :utc}
        {:utc -875836800000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -875836800000, :clock? :utc}
        {:utc -857347200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -857347200000, :clock? :utc}
        {:utc -781056000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc -781056000000, :clock? :utc}
        {:utc -764726400000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -764726400000, :clock? :utc}
        {:utc -744336000000, :clock? :utc}]
       {:day 8, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -744336000000, :clock? :utc}
        {:utc -733795200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -733795200000, :clock? :utc}
        {:utc -716774400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -716774400000, :clock? :utc}
        {:utc -702172800000, :clock? :utc}]
       {:floating-day "Sun>=16",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -654134400000, :clock? :utc}
        {:utc -702172800000, :clock? :utc}]
       {:day 10, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -702172800000, :clock? :utc}
        {:utc -621820800000, :clock? :utc}]
       {:floating-day "Sun>=2",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -621820800000, :clock? :utc}
        {:utc -575424000000, :clock? :utc}]
       {:day 19, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -590025600000, :clock? :utc}
        {:utc -575424000000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -575424000000, :clock? :utc}
        {:utc -235612800000, :clock? :utc}]
       {:day 8, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -235612800000, :clock? :utc}
        {:utc -228268800000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 7, :save 3600000}]
      [[{:utc -228268800000, :clock? :utc}
        {:utc -177724800000, :clock? :utc}]
       {:day 8, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -177724800000, :clock? :utc}
        {:utc -165715200000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -165715200000, :clock? :utc}
        {:utc 10454400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 10454400000, :clock? :utc}
        {:utc 23673600000, :clock? :utc}]
       {:floating-day "Sun>=2",
        :time {:hour 0, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc 23673600000, :clock? :utc}
        {:utc 107917200000, :clock? :utc}]
       {:floating-day "Sun>=2",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 107917200000, :clock? :utc}
        {:utc 121230000000, :clock? :utc}]
       {:day 3, :time {:hour 1, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 121230000000, :clock? :utc}
        {:utc 133927200000, :clock? :utc}]
       {:day 4, :time {:hour 3, :minute 0}, :month 11, :save 0}]
      [[{:utc 133927200000, :clock? :utc}
        {:utc 152686800000, :clock? :utc}]
       {:day 31, :time {:hour 2, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 152686800000, :clock? :utc}
        {:utc 165369600000, :clock? :utc}]
       {:day 3, :time {:hour 5, :minute 0}, :month 11, :save 0}]
      [[{:utc 165369600000, :clock? :utc}
        {:utc 183513600000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 183513600000, :clock? :utc}
        {:utc 202435200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 202435200000, :clock? :utc}
        {:utc 245808000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 245808000000, :clock? :utc}
        {:utc 228700800000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 228700800000, :clock? :utc}
        {:utc 308448000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 291772800000, :clock? :utc}
        {:utc 308448000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 3, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 308448000000, :clock? :utc}
        {:utc 354672000000, :clock? :utc}]
       {:floating-day "Mon>=11",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 354672000000, :clock? :utc}
        {:utc 433900800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 3, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 428457600000, :clock? :utc}
        {:utc 433900800000, :clock? :utc}]
       {:day 31, :time {:hour 0, :minute 0}, :month 7, :save 3600000}]
      [[{:utc 433900800000, :clock? :utc}
        {:utc 482803200000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 482803200000, :clock? :utc}
        {:utc 496713600000, :clock? :utc}]
       {:day 20, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 496713600000, :clock? :utc}
        {:utc 512524800000, :clock? :standard}]
       {:day 28, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 512524800000, :clock? :standard}
        {:utc 528249600000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 764125200000, :clock? :standard}
        {:utc 528249600000, :clock? :standard}]
       {:day 20,
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 528249600000, :clock? :standard}
        {:utc 796176000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 796176000000, :clock? :standard}
        {:utc 846374400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]]},
    "Starke"
    {:current nil,
     :history
     [[[{:utc -702518400000, :clock? :utc}
        {:utc -715824000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -447292800000, :clock? :utc}
        {:utc -715824000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -386812800000, :clock? :utc}
        {:utc -715824000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -715824000000, :clock? :utc}
        {:utc -321494400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "AQ"
    {:current nil,
     :history
     [[[{:utc 57715200000, :clock? :standard}
        {:utc 67996800000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]
      [[{:utc 67996800000, :clock? :standard}
        {:utc 625622400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 2,
        :save 0}]
      [[{:utc 625622400000, :clock? :standard}
        {:utc 636249600000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]]},
    "Poland"
    {:current nil,
     :history
     [[[{:utc -1618696800000, :clock? :standard}
        {:utc -1600466400000, :clock? :standard}]
       {:day 16,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -1600466400000, :clock? :standard}
        {:utc -796600800000, :clock? :utc}]
       {:day 15,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -812502000000, :clock? :standard}
        {:utc -796600800000, :clock? :utc}]
       {:day 3,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -796600800000, :clock? :utc}
        {:utc -778723200000, :clock? :utc}]
       {:day 4, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -778723200000, :clock? :utc}
        {:utc -762652800000, :clock? :utc}]
       {:day 29, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -762652800000, :clock? :utc}
        {:utc -748483200000, :clock? :standard}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc -748483200000, :clock? :standard}
        {:utc -733269600000, :clock? :standard}]
       {:day 14,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -733269600000, :clock? :standard}
        {:utc -715212000000, :clock? :standard}]
       {:day 7,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -715212000000, :clock? :standard}
        {:utc -702259200000, :clock? :standard}]
       {:day 4,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc -684972000000, :clock? :standard}
        {:utc -702259200000, :clock? :standard}]
       {:day 18,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -702259200000, :clock? :standard}
        {:utc -654127200000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -654127200000, :clock? :standard}
        {:utc -386812800000, :clock? :standard}]
       {:day 10,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -397090800000, :clock? :standard}
        {:utc -386812800000, :clock? :standard}]
       {:day 2,
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 6,
        :save 3600000}]
      [[{:utc -386812800000, :clock? :standard}
        {:utc -371084400000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -371084400000, :clock? :standard}
        {:utc -323568000000, :clock? :standard}]
       {:day 30,
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -334191600000, :clock? :standard}
        {:utc -323568000000, :clock? :standard}]
       {:day 31,
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]
      [[{:utc -307580400000, :clock? :standard}
        {:utc -323568000000, :clock? :standard}]
       {:day 3,
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -323568000000, :clock? :standard}
        {:utc -271296000000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -271296000000, :clock? :standard}
        {:utc -228960000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 1, :minute 0, :time-suffix "s"},
        :month 5,
        :save 3600000}]]},
    "NYC"
    {:current nil,
     :history
     [[[{:utc -1570406400000, :clock? :utc}
        {:utc -1551657600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc -1551657600000, :clock? :utc}
        {:utc -1536537600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -1523232000000, :clock? :utc}
        {:utc -1536537600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -1536537600000, :clock? :utc}
        {:utc -447292800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "E-EurAsia"
    {:current
     {:daylight-savings
      {:from 354672000000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 0, :minute 0},
       :month 3,
       :save 3600000},
      :standard
      {:from 846374400000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 0, :minute 0},
       :month 10,
       :save 0}},
     :history
     [[[{:utc 307497600000, :clock? :utc}
        {:utc 354672000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]]},
    "Bahamas"
    {:current nil,
     :history
     [[[{:utc -163641600000, :clock? :utc}
        {:utc -179366400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]]},
    "Phil"
    {:current nil,
     :history
     [[[{:utc -1046649600000, :clock? :utc}
        {:utc -1038700800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 3600000}]
      [[{:utc -1038700800000, :clock? :utc}
        {:utc -496195200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc -496195200000, :clock? :utc}
        {:utc -489283200000, :clock? :utc}]
       {:day 12, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -489283200000, :clock? :utc}
        {:utc 259372800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 7, :save 0}]
      [[{:utc 259372800000, :clock? :utc}
        {:utc 275184000000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]]},
    "US"
    {:current
     {:daylight-savings
      {:from 1173312000000,
       :clock? :utc,
       :floating-day "Sun>=8",
       :time {:hour 2, :minute 0},
       :month 3,
       :save 3600000},
      :standard
      {:from 1193875200000,
       :clock? :utc,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0},
       :month 11,
       :save 0}},
     :history
     [[[{:utc -1633305600000, :clock? :utc}
        {:utc -1615161600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc -1615161600000, :clock? :utc}
        {:utc -880236000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -880236000000, :clock? :utc}
        {:utc -765417600000, :clock? :utc}]
       {:day 9, :time {:hour 2, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -769395600000, :clock? :utc}
        {:utc -765417600000, :clock? :utc}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "u"},
        :month 8,
        :save 3600000}]
      [[{:utc -765417600000, :clock? :utc}
        {:utc -84412800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -84412800000, :clock? :utc}
        {:utc -68688000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 126669600000, :clock? :utc}
        {:utc -68688000000, :clock? :utc}]
       {:day 6, :time {:hour 2, :minute 0}, :month 1, :save 3600000}]
      [[{:utc 162352800000, :clock? :utc}
        {:utc -68688000000, :clock? :utc}]
       {:day 23, :time {:hour 2, :minute 0}, :month 2, :save 3600000}]
      [[{:utc 199238400000, :clock? :utc}
        {:utc -68688000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -68688000000, :clock? :utc}
        {:utc 544233600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 544233600000, :clock? :utc}
        {:utc 1193875200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Mauritius"
    {:current nil,
     :history
     [[[{:utc 403056000000, :clock? :utc}
        {:utc 417052800000, :clock? :utc}]
       {:day 10, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 417052800000, :clock? :utc}
        {:utc 1224979200000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 3, :save 0}]
      [[{:utc 1224979200000, :clock? :utc}
        {:utc 1238284800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 3600000}]]},
    "EgyptAsia"
    {:current nil,
     :history
     [[[{:utc -399081600000, :clock? :utc}
        {:utc -386640000000, :clock? :utc}]
       {:day 10, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -386640000000, :clock? :utc}
        {:utc -368323200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -368323200000, :clock? :utc}
        {:utc -323643600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -323643600000, :clock? :utc}
        {:utc -336783600000, :clock? :utc}]
       {:day 30, :time {:hour 3, :minute 0}, :month 9, :save 0}]
      [[{:utc -102632400000, :clock? :utc}
        {:utc -336783600000, :clock? :utc}]
       {:day 1, :time {:hour 3, :minute 0}, :month 10, :save 0}]]},
    "NC"
    {:current nil,
     :history
     [[[{:utc 249782400000, :clock? :utc}
        {:utc 257385600000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 12,
        :save 3600000}]
      [[{:utc 257385600000, :clock? :utc}
        {:utc 849405600000, :clock? :standard}]
       {:day 27, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 849405600000, :clock? :standard}
        {:utc 857268000000, :clock? :standard}]
       {:day 1,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 12,
        :save 3600000}]]},
    "Macau"
    {:current nil,
     :history
     [[[{:utc -277603200000, :clock? :utc}
        {:utc -257731200000, :clock? :utc}]
       {:floating-day "Sun>=16",
        :time {:hour 3, :minute 30},
        :month 3,
        :save 3600000}]
      [[{:utc -214531200000, :clock? :utc}
        {:utc -257731200000, :clock? :utc}]
       {:floating-day "Sun>=16",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc -257731200000, :clock? :utc}
        {:utc -182908800000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 3, :minute 30},
        :month 11,
        :save 0}]
      [[{:utc -182908800000, :clock? :utc}
        {:utc -131587200000, :clock? :utc}]
       {:floating-day "Sun>=16",
        :time {:hour 3, :minute 30},
        :month 3,
        :save 3600000}]
      [[{:utc -151372800000, :clock? :utc}
        {:utc -131587200000, :clock? :utc}]
       {:floating-day "Sun>=16",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc -131587200000, :clock? :utc}
        {:utc -117158400000, :clock? :utc}]
       {:day 31, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -117158400000, :clock? :utc}
        {:utc -101347200000, :clock? :utc}]
       {:floating-day "Sun>=16",
        :time {:hour 3, :minute 30},
        :month 4,
        :save 3600000}]
      [[{:utc -101347200000, :clock? :utc}
        {:utc 72144000000, :clock? :utc}]
       {:floating-day "Sun>=16",
        :time {:hour 3, :minute 30},
        :month 10,
        :save 0}]
      [[{:utc 87955200000, :clock? :utc}
        {:utc 72144000000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 72144000000, :clock? :utc}
        {:utc 151027200000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 151027200000, :clock? :utc}
        {:utc 166752000000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 3, :minute 30},
        :month 10,
        :save 0}]
      [[{:utc 166752000000, :clock? :utc}
        {:utc 277257600000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 3, :minute 30},
        :month 4,
        :save 3600000}]
      [[{:utc 261446400000, :clock? :utc}
        {:utc 277257600000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Morocco"
    {:current
     {:standard
      {:from 1382832000000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 3, :minute 0},
       :month 10,
       :save 0},
      :daylight-savings
      {:from 1774742400000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 2, :minute 0},
       :month 3,
       :save 3600000}},
     :history
     [[[{:utc -956361600000, :clock? :utc}
        {:utc -950486400000, :clock? :utc}]
       {:day 12, :time {:hour 0, :minute 0}, :month 9, :save 3600000}]
      [[{:utc -950486400000, :clock? :utc}
        {:utc -942019200000, :clock? :utc}]
       {:day 19, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc -942019200000, :clock? :utc}
        {:utc -761184000000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -761184000000, :clock? :utc}
        {:utc -617241600000, :clock? :utc}]
       {:day 18, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc -617241600000, :clock? :utc}
        {:utc -605145600000, :clock? :utc}]
       {:day 11, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -605145600000, :clock? :utc}
        {:utc -81432000000, :clock? :utc}]
       {:day 29, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -81432000000, :clock? :utc}
        {:utc -71107200000, :clock? :utc}]
       {:day 3, :time {:hour 12, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -71107200000, :clock? :utc}
        {:utc 141264000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 141264000000, :clock? :utc}
        {:utc 147225600000, :clock? :utc}]
       {:day 24, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 147225600000, :clock? :utc}
        {:utc 199756800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 207705600000, :clock? :utc}
        {:utc 199756800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 8, :save 0}]
      [[{:utc 199756800000, :clock? :utc}
        {:utc 244252800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 244252800000, :clock? :utc}
        {:utc 265507200000, :clock? :utc}]
       {:day 28, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 265507200000, :clock? :utc}
        {:utc 271036800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 271036800000, :clock? :utc}
        {:utc 1212278400000, :clock? :utc}]
       {:day 4, :time {:hour 0, :minute 0}, :month 8, :save 0}]
      [[{:utc 1212278400000, :clock? :utc}
        {:utc 1220227200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 1220227200000, :clock? :utc}
        {:utc 1243814400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1243814400000, :clock? :utc}
        {:utc 1250812800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 1250812800000, :clock? :utc}
        {:utc 1272758400000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 8, :save 0}]
      [[{:utc 1272758400000, :clock? :utc}
        {:utc 1281225600000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 1281225600000, :clock? :utc}
        {:utc 1301788800000, :clock? :utc}]
       {:day 8, :time {:hour 0, :minute 0}, :month 8, :save 0}]
      [[{:utc 1301788800000, :clock? :utc}
        {:utc 1312070400000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 1312070400000, :clock? :utc}
        {:utc 1345428000000, :clock? :utc}]
       {:day 31, :time {:hour 0, :minute 0}, :month 7, :save 0}]
      [[{:utc 1342753200000, :clock? :utc}
        {:utc 1345428000000, :clock? :utc}]
       {:day 20, :time {:hour 3, :minute 0}, :month 7, :save 0}]
      [[{:utc 1345428000000, :clock? :utc}
        {:utc 1348974000000, :clock? :utc}]
       {:day 20, :time {:hour 2, :minute 0}, :month 8, :save 3600000}]
      [[{:utc 1348974000000, :clock? :utc}
        {:utc 1335657600000, :clock? :utc}]
       {:day 30, :time {:hour 3, :minute 0}, :month 9, :save 0}]
      [[{:utc 1335657600000, :clock? :utc}
        {:utc 1373166000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 1373166000000, :clock? :utc}
        {:utc 1376100000000, :clock? :utc}]
       {:day 7, :time {:hour 3, :minute 0}, :month 7, :save 0}]
      [[{:utc 1376100000000, :clock? :utc}
        {:utc 1403924400000, :clock? :utc}]
       {:day 10, :time {:hour 2, :minute 0}, :month 8, :save 3600000}]
      [[{:utc 1403924400000, :clock? :utc}
        {:utc 1406944800000, :clock? :utc}]
       {:day 28, :time {:hour 3, :minute 0}, :month 6, :save 0}]
      [[{:utc 1406944800000, :clock? :utc}
        {:utc 1434250800000, :clock? :utc}]
       {:day 2, :time {:hour 2, :minute 0}, :month 8, :save 3600000}]
      [[{:utc 1434250800000, :clock? :utc}
        {:utc 1437271200000, :clock? :utc}]
       {:day 14, :time {:hour 3, :minute 0}, :month 6, :save 0}]
      [[{:utc 1437271200000, :clock? :utc}
        {:utc 1465095600000, :clock? :utc}]
       {:day 19, :time {:hour 2, :minute 0}, :month 7, :save 3600000}]
      [[{:utc 1465095600000, :clock? :utc}
        {:utc 1468116000000, :clock? :utc}]
       {:day 5, :time {:hour 3, :minute 0}, :month 6, :save 0}]
      [[{:utc 1468116000000, :clock? :utc}
        {:utc 1495335600000, :clock? :utc}]
       {:day 10, :time {:hour 2, :minute 0}, :month 7, :save 3600000}]
      [[{:utc 1495335600000, :clock? :utc}
        {:utc 1498960800000, :clock? :utc}]
       {:day 21, :time {:hour 3, :minute 0}, :month 5, :save 0}]
      [[{:utc 1498960800000, :clock? :utc}
        {:utc 1526180400000, :clock? :utc}]
       {:day 2, :time {:hour 2, :minute 0}, :month 7, :save 3600000}]
      [[{:utc 1526180400000, :clock? :utc}
        {:utc 1529200800000, :clock? :utc}]
       {:day 13, :time {:hour 3, :minute 0}, :month 5, :save 0}]
      [[{:utc 1529200800000, :clock? :utc}
        {:utc 1557025200000, :clock? :utc}]
       {:day 17, :time {:hour 2, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 1557025200000, :clock? :utc}
        {:utc 1560045600000, :clock? :utc}]
       {:day 5, :time {:hour 3, :minute 0}, :month 5, :save 0}]
      [[{:utc 1560045600000, :clock? :utc}
        {:utc 1587265200000, :clock? :utc}]
       {:day 9, :time {:hour 2, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 1587265200000, :clock? :utc}
        {:utc 1590285600000, :clock? :utc}]
       {:day 19, :time {:hour 3, :minute 0}, :month 4, :save 0}]
      [[{:utc 1590285600000, :clock? :utc}
        {:utc 1618110000000, :clock? :utc}]
       {:day 24, :time {:hour 2, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 1396137600000, :clock? :utc}
        {:utc 1618110000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 1618110000000, :clock? :utc}
        {:utc 1621130400000, :clock? :utc}]
       {:day 11, :time {:hour 3, :minute 0}, :month 4, :save 0}]
      [[{:utc 1621130400000, :clock? :utc}
        {:utc 2107998000000, :clock? :utc}]
       {:day 16, :time {:hour 2, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 1651975200000, :clock? :utc}
        {:utc 2107998000000, :clock? :utc}]
       {:day 8, :time {:hour 2, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 1682215200000, :clock? :utc}
        {:utc 2107998000000, :clock? :utc}]
       {:day 23, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 1713060000000, :clock? :utc}
        {:utc 2107998000000, :clock? :utc}]
       {:day 14, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 1743904800000, :clock? :utc}
        {:utc 2107998000000, :clock? :utc}]
       {:day 6, :time {:hour 2, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 2107998000000, :clock? :utc}
        {:utc 1774742400000, :clock? :utc}]
       {:day 19, :time {:hour 3, :minute 0}, :month 10, :save 0}]
      [[{:utc 2138238000000, :clock? :utc}
        {:utc 1774742400000, :clock? :utc}]
       {:day 4, :time {:hour 3, :minute 0}, :month 10, :save 0}]]},
    "Indianapolis"
    {:current nil,
     :history
     [[[{:utc -900280800000, :clock? :utc}
        {:utc -891820800000, :clock? :utc}]
       {:day 22, :time {:hour 2, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -891820800000, :clock? :utc}
        {:utc -747273600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]]},
    "LH"
    {:current
     {:standard
      {:from 1207008000000,
       :clock? :utc,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0},
       :month 4,
       :save 0},
      :daylight-savings
      {:from 1222819200000,
       :clock? :utc,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0},
       :month 10,
       :save 1800000}},
     :history
     [[[{:utc 372816000000, :clock? :utc}
        {:utc 383788800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 3600000}]
      [[{:utc 383788800000, :clock? :utc}
        {:utc 499219200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc 499219200000, :clock? :utc}
        {:utc 511228800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 1800000}]
      [[{:utc 530071200000, :clock? :utc}
        {:utc 511228800000, :clock? :utc}]
       {:day 19, :time {:hour 2, :minute 0}, :month 10, :save 1800000}]
      [[{:utc 511228800000, :clock? :utc}
        {:utc 562118400000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc 636249600000, :clock? :utc}
        {:utc 562118400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc 562118400000, :clock? :utc}
        {:utc 828230400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 1800000}]
      [[{:utc 967334400000, :clock? :utc}
        {:utc 828230400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 8,
        :save 1800000}]
      [[{:utc 828230400000, :clock? :utc}
        {:utc 1004227200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc 1143849600000, :clock? :utc}
        {:utc 1004227200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 0}]
      [[{:utc 1004227200000, :clock? :utc}
        {:utc 1174780800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 1800000}]
      [[{:utc 1174780800000, :clock? :utc}
        {:utc 1222819200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 0}]]},
    "Austria"
    {:current nil,
     :history
     [[[{:utc -1569708000000, :clock? :standard}
        {:utc -1555797600000, :clock? :standard}]
       {:day 5,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1555797600000, :clock? :standard}
        {:utc -748476000000, :clock? :standard}]
       {:day 13,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -748476000000, :clock? :standard}
        {:utc -733795200000, :clock? :standard}]
       {:day 14,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -717631200000, :clock? :standard}
        {:utc -733795200000, :clock? :standard}]
       {:day 6,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -733795200000, :clock? :standard}
        {:utc -684972000000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -684972000000, :clock? :standard}
        {:utc 338947200000, :clock? :utc}]
       {:day 18,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 323827200000, :clock? :utc}
        {:utc 338947200000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]]},
    "C-Eur"
    {:current
     {:daylight-savings
      {:from 354672000000,
       :clock? :standard,
       :floating-day "lastSun",
       :time {:hour 2, :minute 0, :time-suffix "s"},
       :month 3,
       :save 3600000},
      :standard
      {:from 846374400000,
       :clock? :standard,
       :floating-day "lastSun",
       :time {:hour 2, :minute 0, :time-suffix "s"},
       :month 10,
       :save 0}},
     :history
     [[[{:utc -1693702800000, :clock? :utc}
        {:utc -1680476400000, :clock? :utc}]
       {:day 30, :time {:hour 23, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1680476400000, :clock? :utc}
        {:utc -1663545600000, :clock? :standard}]
       {:day 1, :time {:hour 1, :minute 0}, :month 10, :save 0}]
      [[{:utc -1663545600000, :clock? :standard}
        {:utc -1650326400000, :clock? :standard}]
       {:floating-day "Mon>=15",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -1650326400000, :clock? :standard}
        {:utc -938901600000, :clock? :standard}]
       {:floating-day "Mon>=15",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc -938901600000, :clock? :standard}
        {:utc -857253600000, :clock? :standard}]
       {:day 1,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -857253600000, :clock? :standard}
        {:utc -844556400000, :clock? :standard}]
       {:day 2,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 11,
        :save 0}]
      [[{:utc -844556400000, :clock? :standard}
        {:utc -828223200000, :clock? :standard}]
       {:day 29,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc -828223200000, :clock? :standard}
        {:utc -812678400000, :clock? :standard}]
       {:day 4,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -796773600000, :clock? :standard}
        {:utc -812678400000, :clock? :standard}]
       {:day 2,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc -812678400000, :clock? :standard}
        {:utc -766620000000, :clock? :standard}]
       {:floating-day "Mon>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc -766620000000, :clock? :standard}
        {:utc 228700800000, :clock? :standard}]
       {:day 16,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 243993600000, :clock? :standard}
        {:utc 228700800000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 276055200000, :clock? :standard}
        {:utc 228700800000, :clock? :standard}]
       {:day 1,
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 228700800000, :clock? :standard}
        {:utc 307497600000, :clock? :standard}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 4,
        :save 3600000}]
      [[{:utc 307497600000, :clock? :standard}
        {:utc 354672000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]]},
    "Holiday"
    {:current nil,
     :history
     [[[{:utc 719971200000, :clock? :standard}
        {:utc 730944000000, :clock? :standard}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0, :time-suffix "s"},
        :month 10,
        :save 3600000}]]},
    "Barb"
    {:current nil,
     :history
     [[[{:utc 234928800000, :clock? :utc}
        {:utc 244512000000, :clock? :utc}]
       {:day 12, :time {:hour 2, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 244512000000, :clock? :utc}
        {:utc 261446400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 307504800000, :clock? :utc}
        {:utc 261446400000, :clock? :utc}]
       {:day 30, :time {:hour 2, :minute 0}, :month 9, :save 0}]
      [[{:utc 261446400000, :clock? :utc}
        {:utc 338695200000, :clock? :utc}]
       {:floating-day "Sun>=15",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Jordan"
    {:current
     {:daylight-savings
      {:from 1395878400000,
       :clock? :utc,
       :floating-day "lastThu",
       :time {:hour 24, :minute 0},
       :month 3,
       :save 3600000},
      :standard
      {:from 1414713600000,
       :clock? :standard,
       :floating-day "lastFri",
       :time {:hour 0, :minute 0, :time-suffix "s"},
       :month 10,
       :save 0}},
     :history
     [[[{:utc 108172800000, :clock? :utc}
        {:utc 118281600000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 118281600000, :clock? :utc}
        {:utc 136598400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 215654400000, :clock? :utc}
        {:utc 136598400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 11, :save 0}]
      [[{:utc 136598400000, :clock? :utc}
        {:utc 244512000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 244512000000, :clock? :utc}
        {:utc 262742400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 262742400000, :clock? :utc}
        {:utc 275961600000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 275961600000, :clock? :utc}
        {:utc 481161600000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 481161600000, :clock? :utc}
        {:utc 496972800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 496972800000, :clock? :utc}
        {:utc 512697600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 512697600000, :clock? :utc}
        {:utc 528508800000, :clock? :utc}]
       {:floating-day "Fri>=1",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 610588800000, :clock? :utc}
        {:utc 528508800000, :clock? :utc}]
       {:day 8, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 528508800000, :clock? :utc}
        {:utc 641174400000, :clock? :utc}]
       {:floating-day "Fri>=1",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 641174400000, :clock? :utc}
        {:utc 685929600000, :clock? :utc}]
       {:day 27, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 671846400000, :clock? :utc}
        {:utc 685929600000, :clock? :utc}]
       {:day 17, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 685929600000, :clock? :utc}
        {:utc 702864000000, :clock? :utc}]
       {:day 27, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 702864000000, :clock? :utc}
        {:utc 717897600000, :clock? :utc}]
       {:day 10, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 717897600000, :clock? :utc}
        {:utc 733622400000, :clock? :utc}]
       {:floating-day "Fri>=1",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 779587200000, :clock? :utc}
        {:utc 733622400000, :clock? :utc}]
       {:floating-day "Fri>=15",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 733622400000, :clock? :utc}
        {:utc 811123200000, :clock? :standard}]
       {:floating-day "Fri>=1",
        :time {:hour 0, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 811123200000, :clock? :standard}
        {:utc 930787200000, :clock? :standard}]
       {:floating-day "Fri>=15",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 930787200000, :clock? :standard}
        {:utc 938131200000, :clock? :standard}]
       {:day 1,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 7,
        :save 3600000}]
      [[{:utc 954374400000, :clock? :standard}
        {:utc 938131200000, :clock? :standard}]
       {:floating-day "lastThu",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 3,
        :save 3600000}]
      [[{:utc 938131200000, :clock? :standard}
        {:utc 1017273600000, :clock? :utc}]
       {:floating-day "lastFri",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 1066953600000, :clock? :standard}
        {:utc 1017273600000, :clock? :utc}]
       {:day 24,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 1097798400000, :clock? :standard}
        {:utc 1017273600000, :clock? :utc}]
       {:day 15,
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 1128038400000, :clock? :standard}
        {:utc 1017273600000, :clock? :utc}]
       {:floating-day "lastFri",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 9,
        :save 0}]
      [[{:utc 1161907200000, :clock? :standard}
        {:utc 1017273600000, :clock? :utc}]
       {:floating-day "lastFri",
        :time {:hour 0, :minute 0, :time-suffix "s"},
        :month 10,
        :save 0}]
      [[{:utc 1017273600000, :clock? :utc}
        {:utc 1387497600000, :clock? :utc}]
       {:floating-day "lastThu",
        :time {:hour 24, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 1387497600000, :clock? :utc}
        {:utc 1395878400000, :clock? :utc}]
       {:day 20, :time {:hour 0, :minute 0}, :month 12, :save 0}]]},
    "PRC"
    {:current nil,
     :history
     [[[{:utc 515548800000, :clock? :utc}
        {:utc 526780800000, :clock? :utc}]
       {:day 4, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 526780800000, :clock? :utc}
        {:utc 545011200000, :clock? :utc}]
       {:floating-day "Sun>=11",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]]},
    "HK"
    {:current nil,
     :history
     [[[{:utc -907360200000, :clock? :utc}
        {:utc -891635400000, :clock? :utc}]
       {:day 1, :time {:hour 3, :minute 30}, :month 4, :save 3600000}]
      [[{:utc -891635400000, :clock? :utc}
        {:utc -747952200000, :clock? :utc}]
       {:day 30, :time {:hour 3, :minute 30}, :month 9, :save 0}]
      [[{:utc -747952200000, :clock? :utc}
        {:utc -728512200000, :clock? :utc}]
       {:day 20, :time {:hour 3, :minute 30}, :month 4, :save 3600000}]
      [[{:utc -728512200000, :clock? :utc}
        {:utc -717021000000, :clock? :utc}]
       {:day 1, :time {:hour 3, :minute 30}, :month 12, :save 0}]
      [[{:utc -717021000000, :clock? :utc}
        {:utc -694470600000, :clock? :utc}]
       {:day 13, :time {:hour 3, :minute 30}, :month 4, :save 3600000}]
      [[{:utc -694470600000, :clock? :utc}
        {:utc -683757000000, :clock? :utc}]
       {:day 30, :time {:hour 3, :minute 30}, :month 12, :save 0}]
      [[{:utc -683757000000, :clock? :utc}
        {:utc -668044800000, :clock? :utc}]
       {:day 2, :time {:hour 3, :minute 30}, :month 5, :save 3600000}]
      [[{:utc -668044800000, :clock? :utc}
        {:utc -654912000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 3, :minute 30},
        :month 10,
        :save 0}]
      [[{:utc -542320200000, :clock? :utc}
        {:utc -654912000000, :clock? :utc}]
       {:day 25, :time {:hour 3, :minute 30}, :month 10, :save 0}]
      [[{:utc -654912000000, :clock? :utc}
        {:utc -510179400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 3, :minute 30},
        :month 4,
        :save 3600000}]
      [[{:utc -510179400000, :clock? :utc}
        {:utc -498355200000, :clock? :utc}]
       {:day 1, :time {:hour 3, :minute 30}, :month 11, :save 0}]
      [[{:utc -478729800000, :clock? :utc}
        {:utc -498355200000, :clock? :utc}]
       {:day 31, :time {:hour 3, :minute 30}, :month 10, :save 0}]
      [[{:utc -498355200000, :clock? :utc}
        {:utc -447120000000, :clock? :utc}]
       {:floating-day "Sun>=18",
        :time {:hour 3, :minute 30},
        :month 3,
        :save 3600000}]
      [[{:utc -447120000000, :clock? :utc}
        {:utc 126070200000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 3, :minute 30},
        :month 11,
        :save 0}]
      [[{:utc 126070200000, :clock? :utc}
        {:utc -132883200000, :clock? :utc}]
       {:day 30, :time {:hour 3, :minute 30}, :month 12, :save 3600000}]
      [[{:utc -148694400000, :clock? :utc}
        {:utc -132883200000, :clock? :utc}]
       {:floating-day "Sun>=16",
        :time {:hour 3, :minute 30},
        :month 4,
        :save 3600000}]
      [[{:utc -132883200000, :clock? :utc}
        {:utc 294969600000, :clock? :utc}]
       {:floating-day "Sun>=16",
        :time {:hour 3, :minute 30},
        :month 10,
        :save 0}]
      [[{:utc 294969600000, :clock? :utc}
        {:utc 308880000000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 3, :minute 30},
        :month 5,
        :save 3600000}]]},
    "Menominee"
    {:current nil,
     :history
     [[[{:utc -747273600000, :clock? :utc}
        {:utc -733968000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -733968000000, :clock? :utc}
        {:utc -116467200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -116467200000, :clock? :utc}
        {:utc -100137600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Guat"
    {:current nil,
     :history
     [[[{:utc 123033600000, :clock? :utc}
        {:utc 130896000000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 11, :save 3600000}]
      [[{:utc 130896000000, :clock? :utc}
        {:utc 422323200000, :clock? :utc}]
       {:day 24, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 422323200000, :clock? :utc}
        {:utc 433036800000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 433036800000, :clock? :utc}
        {:utc 669686400000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 669686400000, :clock? :utc}
        {:utc 684201600000, :clock? :utc}]
       {:day 23, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc 684201600000, :clock? :utc}
        {:utc 1146355200000, :clock? :utc}]
       {:day 7, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 1146355200000, :clock? :utc}
        {:utc 1159660800000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]]},
    "CO"
    {:current nil,
     :history
     [[[{:utc 704851200000, :clock? :utc}
        {:utc 733881600000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]]},
    "Lebanon"
    {:current
     {:daylight-savings
      {:from 733276800000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 0, :minute 0},
       :month 3,
       :save 3600000},
      :standard
      {:from 941328000000,
       :clock? :utc,
       :floating-day "lastSun",
       :time {:hour 0, :minute 0},
       :month 10,
       :save 0}},
     :history
     [[[{:utc -1570406400000, :clock? :utc}
        {:utc -1552176000000, :clock? :utc}]
       {:day 28, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -1552176000000, :clock? :utc}
        {:utc -1538352000000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -1538352000000, :clock? :utc}
        {:utc -1522540800000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1522540800000, :clock? :utc}
        {:utc -1507507200000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -1507507200000, :clock? :utc}
        {:utc -1490572800000, :clock? :utc}]
       {:day 26, :time {:hour 0, :minute 0}, :month 3, :save 3600000}]
      [[{:utc -1490572800000, :clock? :utc}
        {:utc -1473638400000, :clock? :utc}]
       {:day 8, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -1473638400000, :clock? :utc}
        {:utc -1460937600000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1460937600000, :clock? :utc}
        {:utc -399859200000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -399859200000, :clock? :utc}
        {:utc -386640000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -386640000000, :clock? :utc}
        {:utc 78019200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 78019200000, :clock? :utc}
        {:utc 86745600000, :clock? :utc}]
       {:day 22, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 86745600000, :clock? :utc}
        {:utc 105062400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 105062400000, :clock? :utc}
        {:utc 275961600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 262742400000, :clock? :utc}
        {:utc 275961600000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 275961600000, :clock? :utc}
        {:utc 452217600000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc 452217600000, :clock? :utc}
        {:utc 466732800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 581126400000, :clock? :utc}
        {:utc 466732800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc 610761600000, :clock? :utc}
        {:utc 466732800000, :clock? :utc}]
       {:day 10, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 466732800000, :clock? :utc}
        {:utc 641520000000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 641520000000, :clock? :utc}
        {:utc 718156800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc 718156800000, :clock? :utc}
        {:utc 733276800000, :clock? :utc}]
       {:day 4, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 749001600000, :clock? :utc}
        {:utc 733276800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]]},
    "Swiss"
    {:current nil,
     :history
     [[[{:utc -904780800000, :clock? :utc}
        {:utc -891561600000, :clock? :utc}]
       {:floating-day "Mon>=1",
        :time {:hour 1, :minute 0},
        :month 5,
        :save 3600000}]]},
    "Halifax"
    {:current nil,
     :history
     [[[{:utc -1696291200000, :clock? :utc}
        {:utc -1680480000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1680480000000, :clock? :utc}
        {:utc -1566777600000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -1566777600000, :clock? :utc}
        {:utc -1557100800000, :clock? :utc}]
       {:day 9, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1557100800000, :clock? :utc}
        {:utc -1535500800000, :clock? :utc}]
       {:day 29, :time {:hour 0, :minute 0}, :month 8, :save 0}]
      [[{:utc -1535500800000, :clock? :utc}
        {:utc -1524960000000, :clock? :utc}]
       {:day 6, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1524960000000, :clock? :utc}
        {:utc -1504483200000, :clock? :utc}]
       {:day 5, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -1504483200000, :clock? :utc}
        {:utc -1461974400000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1461974400000, :clock? :utc}
        {:utc -1472860800000, :clock? :utc}]
       {:day 4, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -1429401600000, :clock? :utc}
        {:utc -1472860800000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -1472860800000, :clock? :utc}
        {:utc -1396742400000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc -1396742400000, :clock? :utc}
        {:utc -1376870400000, :clock? :utc}]
       {:day 28, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -1376870400000, :clock? :utc}
        {:utc -1366502400000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1366502400000, :clock? :utc}
        {:utc -1346630400000, :clock? :utc}]
       {:day 13, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -1346630400000, :clock? :utc}
        {:utc -1333843200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1333843200000, :clock? :utc}
        {:utc -1314403200000, :clock? :utc}]
       {:day 26, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -1303689600000, :clock? :utc}
        {:utc -1314403200000, :clock? :utc}]
       {:day 9, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -1272672000000, :clock? :utc}
        {:utc -1314403200000, :clock? :utc}]
       {:day 3, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -1240099200000, :clock? :utc}
        {:utc -1314403200000, :clock? :utc}]
       {:day 15, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -1314403200000, :clock? :utc}
        {:utc -1207785600000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 0, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc -1207785600000, :clock? :utc}
        {:utc -1188777600000, :clock? :utc}]
       {:floating-day "Mon>=24",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -1188777600000, :clock? :utc}
        {:utc -1143936000000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1157328000000, :clock? :utc}
        {:utc -1143936000000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc -1143936000000, :clock? :utc}
        {:utc -1124064000000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc -1124064000000, :clock? :utc}
        {:utc -1113782400000, :clock? :utc}]
       {:day 20, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1113782400000, :clock? :utc}
        {:utc -1091404800000, :clock? :utc}]
       {:day 16, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -1091404800000, :clock? :utc}
        {:utc -1081036800000, :clock? :utc}]
       {:day 2, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -1081036800000, :clock? :utc}
        {:utc -1059868800000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -1059868800000, :clock? :utc}
        {:utc -1050796800000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 6, :save 3600000}]
      [[{:utc -1050796800000, :clock? :utc}
        {:utc -1031011200000, :clock? :utc}]
       {:day 14, :time {:hour 0, :minute 0}, :month 9, :save 0}]
      [[{:utc -1031011200000, :clock? :utc}
        {:utc -1018396800000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc -965606400000, :clock? :utc}
        {:utc -1018396800000, :clock? :utc}]
       {:day 28, :time {:hour 0, :minute 0}, :month 5, :save 3600000}]
      [[{:utc -1018396800000, :clock? :utc}
        {:utc -936316800000, :clock? :utc}]
       {:floating-day "Mon>=24",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -936316800000, :clock? :utc}
        {:utc -733968000000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 0, :minute 0},
        :month 5,
        :save 3600000}]
      [[{:utc -747273600000, :clock? :utc}
        {:utc -733968000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -733968000000, :clock? :utc}
        {:utc -589420800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -589420800000, :clock? :utc}
        {:utc -576115200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -576115200000, :clock? :utc}
        {:utc -431568000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -431568000000, :clock? :utc}
        {:utc -418262400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -418262400000, :clock? :utc}
        {:utc -242265600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -242265600000, :clock? :utc}
        {:utc -226540800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]},
    "Cook"
    {:current nil,
     :history
     [[[{:utc 279676800000, :clock? :utc}
        {:utc 289094400000, :clock? :utc}]
       {:day 12, :time {:hour 0, :minute 0}, :month 11, :save 1800000}]
      [[{:utc 309916800000, :clock? :utc}
        {:utc 289094400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 1800000}]]},
    "Mongol"
    {:current nil,
     :history
     [[[{:utc 433814400000, :clock? :utc}
        {:utc 418003200000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 10, :save 0}]
      [[{:utc 418003200000, :clock? :utc}
        {:utc 465350400000, :clock? :utc}]
       {:day 1, :time {:hour 0, :minute 0}, :month 4, :save 3600000}]
      [[{:utc 481075200000, :clock? :utc}
        {:utc 465350400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 465350400000, :clock? :utc}
        {:utc 988416000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 988416000000, :clock? :utc}
        {:utc 1001721600000, :clock? :utc}]
       {:floating-day "lastSat",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 1001721600000, :clock? :utc}
        {:utc 1017446400000, :clock? :utc}]
       {:floating-day "lastSat",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 1017446400000, :clock? :utc}
        {:utc 1443225600000, :clock? :utc}]
       {:floating-day "lastSat",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 1427500800000, :clock? :utc}
        {:utc 1443225600000, :clock? :utc}]
       {:floating-day "lastSat",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 3600000}]]},
    "SovietZone"
    {:current nil,
     :history
     [[[{:utc -776556000000, :clock? :utc}
        {:utc -761176800000, :clock? :standard}]
       {:day 24, :time {:hour 2, :minute 0}, :month 5, :save 7200000}]
      [[{:utc -765925200000, :clock? :utc}
        {:utc -761176800000, :clock? :standard}]
       {:day 24, :time {:hour 3, :minute 0}, :month 9, :save 3600000}]]},
    "Thule"
    {:current
     {:daylight-savings
      {:from 1173312000000,
       :clock? :utc,
       :floating-day "Sun>=8",
       :time {:hour 2, :minute 0},
       :month 3,
       :save 3600000},
      :standard
      {:from 1193875200000,
       :clock? :utc,
       :floating-day "Sun>=1",
       :time {:hour 2, :minute 0},
       :month 11,
       :save 0}},
     :history
     [[[{:utc 670377600000, :clock? :utc}
        {:utc 686102400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 3,
        :save 3600000}]
      [[{:utc 686102400000, :clock? :utc}
        {:utc 733622400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc 733622400000, :clock? :utc}
        {:utc 752025600000, :clock? :utc}]
       {:floating-day "Sun>=1",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc 752025600000, :clock? :utc}
        {:utc 1173312000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]]},
    "Vanuatu"
    {:current nil,
     :history
     [[[{:utc 433296000000, :clock? :utc}
        {:utc 448848000000, :clock? :utc}]
       {:day 25, :time {:hour 0, :minute 0}, :month 9, :save 3600000}]
      [[{:utc 467337600000, :clock? :utc}
        {:utc 448848000000, :clock? :utc}]
       {:day 23, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc 448848000000, :clock? :utc}
        {:utc 496281600000, :clock? :utc}]
       {:floating-day "Sun>=23",
        :time {:hour 0, :minute 0},
        :month 3,
        :save 0}]
      [[{:utc 496281600000, :clock? :utc}
        {:utc 696124800000, :clock? :utc}]
       {:floating-day "Sun>=23",
        :time {:hour 0, :minute 0},
        :month 9,
        :save 3600000}]
      [[{:utc 719798400000, :clock? :utc}
        {:utc 696124800000, :clock? :utc}]
       {:floating-day "Sun>=23",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 3600000}]]},
    "DR"
    {:current nil,
     :history
     [[[{:utc -100137600000, :clock? :utc}
        {:utc -89683200000, :clock? :utc}]
       {:day 30, :time {:hour 0, :minute 0}, :month 10, :save 3600000}]
      [[{:utc -89683200000, :clock? :utc}
        {:utc -5788800000, :clock? :utc}]
       {:day 28, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 4406400000, :clock? :utc} {:utc -5788800000, :clock? :utc}]
       {:day 21, :time {:hour 0, :minute 0}, :month 2, :save 0}]
      [[{:utc 33177600000, :clock? :utc}
        {:utc -5788800000, :clock? :utc}]
       {:day 20, :time {:hour 0, :minute 0}, :month 1, :save 0}]
      [[{:utc -5788800000, :clock? :utc}
        {:utc 64800000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 0, :minute 0},
        :month 10,
        :save 1800000}]]},
    "Edm"
    {:current nil,
     :history
     [[[{:utc -1615154400000, :clock? :utc}
        {:utc -1632614400000, :clock? :utc}]
       {:day 27, :time {:hour 2, :minute 0}, :month 10, :save 0}]
      [[{:utc -1632614400000, :clock? :utc}
        {:utc -1596837600000, :clock? :utc}]
       {:floating-day "Sun>=8",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -1596837600000, :clock? :utc}
        {:utc -1567987200000, :clock? :utc}]
       {:day 27, :time {:hour 2, :minute 0}, :month 5, :save 0}]
      [[{:utc -1551657600000, :clock? :utc}
        {:utc -1567987200000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -1567987200000, :clock? :utc}
        {:utc -1523232000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -1523232000000, :clock? :utc}
        {:utc -880236000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -880236000000, :clock? :utc}
        {:utc -765417600000, :clock? :utc}]
       {:day 9, :time {:hour 2, :minute 0}, :month 2, :save 3600000}]
      [[{:utc -769395600000, :clock? :utc}
        {:utc -765417600000, :clock? :utc}]
       {:day 14,
        :time {:hour 23, :minute 0, :time-suffix "u"},
        :month 8,
        :save 3600000}]
      [[{:utc -765417600000, :clock? :utc}
        {:utc -715824000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -715824000000, :clock? :utc}
        {:utc -702518400000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -702518400000, :clock? :utc}
        {:utc -84412800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 9,
        :save 0}]
      [[{:utc -84412800000, :clock? :utc}
        {:utc -68688000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -68688000000, :clock? :utc}
        {:utc -21513600000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc -21513600000, :clock? :utc}
        {:utc -5788800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]
      [[{:utc -5788800000, :clock? :utc}
        {:utc 73440000000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 10,
        :save 0}]
      [[{:utc 73440000000, :clock? :utc}
        {:utc 89164800000, :clock? :utc}]
       {:floating-day "lastSun",
        :time {:hour 2, :minute 0},
        :month 4,
        :save 3600000}]]}}})
