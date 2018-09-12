(ns vura.timezones.db
  (:require [clojure.string]))

(declare db locales)


(defn get-rule [rule]
  (get-in db [:rules rule]))


(defn get-timezone [zone-name]
  (if-let [zone' (get-in db [:zones zone-name])]
    (if (string? zone') (get-timezone zone')
      zone')
    (throw
      (ex-info "No such zone."
      {:timezone zone-name
       :available-timezones (keys (:zones db))}))))


(def available-zones
  (sort (keys (:zones db))))


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


(defn get-timezone-locale [timezone]
  (get timezone->locale timezone))


(defn get-locale-timezone [locale]
  (get-in 
    locales 
    [(clojure.string/upper-case locale) :zone]))


(defn get-locale [locale]
  (get locales (clojure.string/upper-case locale)))



(def db-extensions
  {:zones 
   {"GMT"
    {:offset 0, :rule "-" , :format "GMT", :from 0}}})

(def db
  (merge-with merge
              db-extensions
              {:zones
               {"Asia/Rangoon" "Asia/Yangon",
                "Mexico/BajaNorte" "America/Tijuana",
                "Europe/Amsterdam"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 220924800000},
                "America/Montevideo"
                {:offset -10800000,
                 :rule "Uruguay",
                 :format "-03/-02",
                 :from 156902400000},
                "Africa/Kinshasa" "Africa/Lagos",
                "Asia/Sakhalin"
                {:offset 39600000, :rule "-", :format "+11", :from 1459044000000},
                "America/Santarem"
                {:offset -10800000, :rule "-", :format "-03", :from 1214265600000},
                "Europe/Tiraspol" "Europe/Chisinau",
                "Africa/Ouagadougou" "Africa/Abidjan",
                "Asia/Amman"
                {:offset 7200000,
                 :rule "Jordan",
                 :format "EE%sT",
                 :from -1230768000000},
                "Europe/Tallinn"
                {:offset 7200000, :rule "EU", :format "EE%sT", :from 1014249600000},
                "Africa/Blantyre" "Africa/Maputo",
                "America/Nipigon"
                {:offset -18000000,
                 :rule "Canada",
                 :format "E%sT",
                 :from -880236000000},
                "America/Jamaica"
                {:offset -18000000, :rule "-", :format "EST", :from 441763200000},
                "Africa/El_Aaiun"
                {:offset 0, :rule "Morocco", :format "WE%sT", :from 198288000000},
                "Australia/LHI" "Australia/Lord_Howe",
                "America/Costa_Rica"
                {:offset -21600000,
                 :rule "CR",
                 :format "C%sT",
                 :from -1545091200000},
                "Canada/Eastern" "America/Toronto",
                "Asia/Kuala_Lumpur"
                {:offset 28800000, :rule "-", :format "+08", :from 378691200000},
                "America/Cordoba" "America/Argentina/Cordoba",
                "Asia/Kuwait" "Asia/Riyadh",
                "Asia/Tokyo"
                {:offset 32400000,
                 :rule "Japan",
                 :format "J%sT",
                 :from -2587766400000},
                "America/Maceio"
                {:offset -10800000, :rule "-", :format "-03", :from 1033430400000},
                "Europe/Brussels"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 220924800000},
                "America/Nome"
                {:offset -32400000, :rule "US", :format "AK%sT", :from 438998400000},
                "Australia/Broken_Hill"
                {:offset 34200000, :rule "AS", :format "AC%sT", :from 946684800000},
                "Asia/Chita"
                {:offset 32400000, :rule "-", :format "+09", :from 1459044000000},
                "Universal" "Etc/UTC",
                "Africa/Timbuktu" "Africa/Abidjan",
                "Africa/Casablanca"
                {:offset 0, :rule "Morocco", :format "WE%sT", :from 504921600000},
                "Europe/Isle_of_Man" "Europe/London",
                "America/Ensenada" "America/Tijuana",
                "America/Argentina/San_Luis"
                {:offset -10800000, :rule "-", :format "-03", :from 1255219200000},
                "America/St_Johns"
                {:offset -12600000,
                 :rule "Canada",
                 :format "N%sT",
                 :from 1320105600000},
                "Europe/Berlin"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 315532800000},
                "Africa/Lome" "Africa/Abidjan",
                "America/North_Dakota/Center"
                {:offset -21600000, :rule "US", :format "C%sT", :from 719978400000},
                "Asia/Aqtobe"
                {:offset 18000000, :rule "-", :format "+05", :from 1099188000000},
                "Antarctica/Macquarie"
                {:offset 39600000, :rule "-", :format "+11", :from 1270350000000},
                "Asia/Seoul"
                {:offset 32400000, :rule "ROK", :format "K%sT", :from -264902400000},
                "Chile/EasterIsland" "Pacific/Easter",
                "US/Hawaii" "Pacific/Honolulu",
                "America/Punta_Arenas"
                {:offset -10800000, :rule "-", :format "-03", :from 1480809600000},
                "Indian/Christmas"
                {:offset 25200000, :rule "-", :format "+07", :from -2364076800000},
                "Atlantic/Madeira"
                {:offset 0, :rule "EU", :format "WE%sT", :from 433299600000},
                "Asia/Barnaul"
                {:offset 25200000, :rule "-", :format "+07", :from 1459044000000},
                "Asia/Colombo"
                {:offset 19800000, :rule "-", :format "+0530", :from 1145061000000},
                "America/Argentina/Mendoza"
                {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
                "Asia/Novokuznetsk"
                {:offset 25200000, :rule "-", :format "+07", :from 1301191200000},
                "America/Indiana/Vevay"
                {:offset -18000000, :rule "US", :format "E%sT", :from 1136073600000},
                "America/Argentina/Rio_Gallegos"
                {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
                "Etc/GMT" {:offset 0, :rule "-", :format "GMT", :from nil},
                "US/Aleutian" "America/Adak",
                "America/Fortaleza"
                {:offset -10800000, :rule "-", :format "-03", :from 1033430400000},
                "Australia/South" "Australia/Adelaide",
                "Europe/Uzhgorod"
                {:offset 7200000, :rule "EU", :format "EE%sT", :from 788918400000},
                "Asia/Beirut"
                {:offset 7200000,
                 :rule "Lebanon",
                 :format "EE%sT",
                 :from -2840140800000},
                "Australia/Yancowinna" "Australia/Broken_Hill",
                "Africa/Sao_Tome"
                {:offset 3600000, :rule "-", :format "WAT", :from 1514768400000},
                "GMT" "Etc/GMT",
                "Cuba" "America/Havana",
                "Asia/Dubai"
                {:offset 14400000, :rule "-", :format "+04", :from -1577923200000},
                "Europe/Stockholm"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 315532800000},
                "Portugal" "Europe/Lisbon",
                "America/Boise"
                {:offset -25200000, :rule "US", :format "M%sT", :from 129088800000},
                "America/Scoresbysund"
                {:offset -3600000,
                 :rule "EU",
                 :format "-01/+00",
                 :from 354672000000},
                "Indian/Maldives"
                {:offset 18000000, :rule "-", :format "+05", :from -315619200000},
                "Europe/Simferopol"
                {:offset 10800000, :rule "-", :format "MSK", :from 1414288800000},
                "Pacific/Efate"
                {:offset 39600000,
                 :rule "Vanuatu",
                 :format "+11/+12",
                 :from -1829347200000},
                "GMT-0" "Etc/GMT",
                "Asia/Qatar"
                {:offset 10800000, :rule "-", :format "+03", :from 76204800000},
                "GMT+0" "Etc/GMT",
                "GMT0" "Etc/GMT",
                "Europe/Jersey" "Europe/London",
                "Pacific/Honolulu"
                {:offset -36000000, :rule "-", :format "HST", :from -712188000000},
                "America/Tegucigalpa"
                {:offset -21600000,
                 :rule "Hond",
                 :format "C%sT",
                 :from -1538524800000},
                "Atlantic/South_Georgia"
                {:offset -7200000, :rule "-", :format "-02", :from -2524521600000},
                "Asia/Hong_Kong"
                {:offset 28800000, :rule "HK", :format "HK%sT", :from -766713600000},
                "Pacific/Pohnpei"
                {:offset 39600000, :rule "-", :format "+11", :from -2177452800000},
                "Pacific/Norfolk"
                {:offset 39600000, :rule "-", :format "+11", :from 1443924000000},
                "America/Santiago"
                {:offset -14400000,
                 :rule "Chile",
                 :format "-04/-03",
                 :from -713667600000},
                "America/Indiana/Knox"
                {:offset -21600000, :rule "US", :format "C%sT", :from 1143943200000},
                "Etc/GMT0" "Etc/GMT",
                "Australia/Brisbane"
                {:offset 36000000, :rule "AQ", :format "AE%sT", :from 31536000000},
                "America/Anguilla" "America/Port_of_Spain",
                "Asia/Hovd"
                {:offset 25200000,
                 :rule "Mongol",
                 :format "+07/+08",
                 :from 252460800000},
                "Asia/Jakarta"
                {:offset 25200000, :rule "-", :format "WIB", :from -189388800000},
                "Atlantic/Stanley"
                {:offset -10800000, :rule "-", :format "-03", :from 1283652000000},
                "America/Jujuy" "America/Argentina/Jujuy",
                "Indian/Comoro" "Africa/Nairobi",
                "America/Sao_Paulo"
                {:offset -10800000,
                 :rule "Brazil",
                 :format "-03/-02",
                 :from -189388800000},
                "Asia/Macao" "Asia/Macau",
                "Etc/GMT+12"
                {:offset -43200000, :rule "-", :format "-12", :from nil},
                "Asia/Macau"
                {:offset 28800000,
                 :rule "Macau",
                 :format "C%sT",
                 :from -1830470400000},
                "America/Menominee"
                {:offset -21600000, :rule "US", :format "C%sT", :from 104896800000},
                "Asia/Bahrain" "Asia/Qatar",
                "Asia/Katmandu" "Asia/Kathmandu",
                "America/Montreal" "America/Toronto",
                "Atlantic/St_Helena" "Africa/Abidjan",
                "MET" {:offset 3600000, :rule "C-Eur", :format "ME%sT", :from nil},
                "Africa/Tunis"
                {:offset 3600000,
                 :rule "Tunisia",
                 :format "CE%sT",
                 :from -1855958400000},
                "Africa/Lusaka" "Africa/Maputo",
                "Etc/GMT+2" {:offset -7200000, :rule "-", :format "-02", :from nil},
                "CST6CDT" {:offset -21600000, :rule "US", :format "C%sT", :from nil},
                "Pacific/Easter"
                {:offset -21600000,
                 :rule "Chile",
                 :format "-06/-05",
                 :from 384922800000},
                "Asia/Ashkhabad" "Asia/Ashgabat",
                "Africa/Cairo"
                {:offset 7200000,
                 :rule "Egypt",
                 :format "EE%sT",
                 :from -2185401600000},
                "Etc/GMT-10" {:offset 36000000, :rule "-", :format "+10", :from nil},
                "Pacific/Chuuk"
                {:offset 36000000, :rule "-", :format "+10", :from -2177452800000},
                "US/Eastern" "America/New_York",
                "America/Montserrat" "America/Port_of_Spain",
                "Pacific/Tarawa"
                {:offset 43200000, :rule "-", :format "+12", :from -2177452800000},
                "America/Resolute"
                {:offset -21600000,
                 :rule "Canada",
                 :format "C%sT",
                 :from 1173582000000},
                "Pacific/Saipan" "Pacific/Guam",
                "Asia/Thimbu" "Asia/Thimphu",
                "Asia/Muscat" "Asia/Dubai",
                "Asia/Saigon" "Asia/Ho_Chi_Minh",
                "Hongkong" "Asia/Hong_Kong",
                "Europe/Zaporozhye"
                {:offset 7200000, :rule "EU", :format "EE%sT", :from 788918400000},
                "Asia/Aqtau"
                {:offset 18000000, :rule "-", :format "+05", :from 1099188000000},
                "Asia/Hebron"
                {:offset 7200000,
                 :rule "Palestine",
                 :format "EE%sT",
                 :from 915148800000},
                "Asia/Kuching"
                {:offset 28800000, :rule "-", :format "+08", :from -766972800000},
                "Pacific/Tahiti"
                {:offset -36000000, :rule "-", :format "-10", :from -1806710400000},
                "America/Argentina/Jujuy"
                {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
                "Etc/GMT+1" {:offset -3600000, :rule "-", :format "-01", :from nil},
                "Atlantic/Faroe"
                {:offset 0, :rule "EU", :format "WE%sT", :from 347155200000},
                "Asia/Vladivostok"
                {:offset 36000000, :rule "-", :format "+10", :from 1414288800000},
                "Europe/Oslo"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 315532800000},
                "Australia/Tasmania" "Australia/Hobart",
                "Asia/Oral"
                {:offset 18000000, :rule "-", :format "+05", :from 1099188000000},
                "Arctic/Longyearbyen" "Europe/Oslo",
                "America/Indiana/Marengo"
                {:offset -18000000, :rule "US", :format "E%sT", :from 1136073600000},
                "America/Rainy_River"
                {:offset -21600000,
                 :rule "Canada",
                 :format "C%sT",
                 :from -880236000000},
                "America/Barbados"
                {:offset -14400000,
                 :rule "Barb",
                 :format "A%sT",
                 :from -1199232000000},
                "Indian/Chagos"
                {:offset 21600000, :rule "-", :format "+06", :from 820454400000},
                "Indian/Cocos"
                {:offset 23400000, :rule "-", :format "+0630", :from -2208988800000},
                "Australia/Victoria" "Australia/Melbourne",
                "Africa/Harare" "Africa/Maputo",
                "America/Aruba" "America/Curacao",
                "America/El_Salvador"
                {:offset -21600000,
                 :rule "Salv",
                 :format "C%sT",
                 :from -1546300800000},
                "America/Porto_Velho"
                {:offset -14400000, :rule "-", :format "-04", :from 590025600000},
                "Etc/GMT-4" {:offset 14400000, :rule "-", :format "+04", :from nil},
                "Navajo" "America/Denver",
                "Australia/Hobart"
                {:offset 36000000, :rule "AT", :format "AE%sT", :from -94694400000},
                "America/Ojinaga"
                {:offset -25200000, :rule "US", :format "M%sT", :from 1262304000000},
                "Asia/Jayapura"
                {:offset 32400000, :rule "-", :format "WIT", :from -189388800000},
                "Etc/Universal" "Etc/UTC",
                "America/Goose_Bay"
                {:offset -14400000,
                 :rule "Canada",
                 :format "A%sT",
                 :from 1320105600000},
                "America/Panama"
                {:offset -18000000, :rule "-", :format "EST", :from -1946937600000},
                "Africa/Dar_es_Salaam" "Africa/Nairobi",
                "America/Mendoza" "America/Argentina/Mendoza",
                "Asia/Tehran"
                {:offset 12600000,
                 :rule "Iran",
                 :format "+0330/+0430",
                 :from 283996800000},
                "America/Blanc-Sablon"
                {:offset -14400000, :rule "-", :format "AST", :from 0},
                "PST8PDT" {:offset -28800000, :rule "US", :format "P%sT", :from nil},
                "Pacific/Johnston" "Pacific/Honolulu",
                "Etc/GMT-12" {:offset 43200000, :rule "-", :format "+12", :from nil},
                "America/Noronha"
                {:offset -7200000, :rule "-", :format "-02", :from 1033430400000},
                "America/St_Kitts" "America/Port_of_Spain",
                "Etc/GMT+10"
                {:offset -36000000, :rule "-", :format "-10", :from nil},
                "Pacific/Enderbury"
                {:offset 46800000, :rule "-", :format "+13", :from 788832000000},
                "Australia/Queensland" "Australia/Brisbane",
                "Etc/GMT+5" {:offset -18000000, :rule "-", :format "-05", :from nil},
                "Pacific/Ponape" "Pacific/Pohnpei",
                "Antarctica/McMurdo" "Pacific/Auckland",
                "Africa/Ceuta"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 504921600000},
                "Asia/Baku"
                {:offset 14400000,
                 :rule "Azer",
                 :format "+04/+05",
                 :from 852076800000},
                "Europe/Sofia"
                {:offset 7200000, :rule "EU", :format "EE%sT", :from 852076800000},
                "CET" {:offset 3600000, :rule "C-Eur", :format "CE%sT", :from nil},
                "America/Pangnirtung"
                {:offset -18000000,
                 :rule "Canada",
                 :format "E%sT",
                 :from 972784800000},
                "America/Monterrey"
                {:offset -21600000,
                 :rule "Mexico",
                 :format "C%sT",
                 :from 599616000000},
                "Asia/Pontianak"
                {:offset 25200000, :rule "-", :format "WIB", :from 567993600000},
                "America/Moncton"
                {:offset -14400000,
                 :rule "Canada",
                 :format "A%sT",
                 :from 1167609600000},
                "Europe/Belfast" "Europe/London",
                "America/Dominica" "America/Port_of_Spain",
                "America/Swift_Current"
                {:offset -21600000, :rule "-", :format "CST", :from 73447200000},
                "Eire" "Europe/Dublin",
                "America/Argentina/Salta"
                {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
                "America/Sitka"
                {:offset -32400000, :rule "US", :format "AK%sT", :from 438998400000},
                "America/Creston"
                {:offset -25200000, :rule "-", :format "MST", :from -1627862400000},
                "America/St_Vincent" "America/Port_of_Spain",
                "Europe/Andorra"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 481082400000},
                "Pacific/Wallis"
                {:offset 43200000, :rule "-", :format "+12", :from -2177452800000},
                "Asia/Ujung_Pandang" "Asia/Makassar",
                "Africa/Niamey" "Africa/Lagos",
                "MST7MDT" {:offset -25200000, :rule "US", :format "M%sT", :from nil},
                "Asia/Manila"
                {:offset 28800000,
                 :rule "Phil",
                 :format "+08/+09",
                 :from -794188800000},
                "HST" {:offset -36000000, :rule "-", :format "HST", :from nil},
                "Asia/Shanghai"
                {:offset 28800000, :rule "PRC", :format "C%sT", :from -662688000000},
                "Etc/GMT+4" {:offset -14400000, :rule "-", :format "-04", :from nil},
                "Europe/London"
                {:offset 0, :rule "EU", :format "GMT/BST", :from 820454400000},
                "Europe/Vienna"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 347155200000},
                "Jamaica" "America/Jamaica",
                "Africa/Kigali" "Africa/Maputo",
                "Etc/GMT-0" "Etc/GMT",
                "Libya" "Africa/Tripoli",
                "Pacific/Auckland"
                {:offset 43200000, :rule "NZ", :format "NZ%sT", :from -757382400000},
                "EET" {:offset 7200000, :rule "EU", :format "EE%sT", :from nil},
                "Europe/Luxembourg"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 220924800000},
                "Europe/Bucharest"
                {:offset 7200000, :rule "EU", :format "EE%sT", :from 852076800000},
                "Asia/Khandyga"
                {:offset 32400000, :rule "-", :format "+09", :from 1414288800000},
                "Asia/Kolkata"
                {:offset 19800000, :rule "-", :format "IST", :from -764121600000},
                "America/Louisville" "America/Kentucky/Louisville",
                "America/Atka" "America/Adak",
                "Etc/UTC" {:offset 0, :rule "-", :format "UTC", :from nil},
                "Pacific/Gambier"
                {:offset -32400000, :rule "-", :format "-09", :from -1806710400000},
                "Asia/Dushanbe"
                {:offset 18000000, :rule "-", :format "+05", :from 684381600000},
                "Asia/Choibalsan"
                {:offset 28800000,
                 :rule "Mongol",
                 :format "+08/+09",
                 :from 1206921600000},
                "Asia/Phnom_Penh" "Asia/Bangkok",
                "Pacific/Guadalcanal"
                {:offset 39600000, :rule "-", :format "+11", :from -1806710400000},
                "US/Arizona" "America/Phoenix",
                "Pacific/Yap" "Pacific/Chuuk",
                "Europe/Tirane"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 457488000000},
                "Africa/Maseru" "Africa/Johannesburg",
                "America/Cambridge_Bay"
                {:offset -25200000,
                 :rule "Canada",
                 :format "M%sT",
                 :from 986094000000},
                "America/Denver"
                {:offset -25200000, :rule "US", :format "M%sT", :from -94694400000},
                "America/Thule"
                {:offset -14400000,
                 :rule "Thule",
                 :format "A%sT",
                 :from -1686096000000},
                "America/St_Barthelemy" "America/Port_of_Spain",
                "Etc/GMT+3" {:offset -10800000, :rule "-", :format "-03", :from nil},
                "Asia/Srednekolymsk"
                {:offset 39600000, :rule "-", :format "+11", :from 1414288800000},
                "America/Recife"
                {:offset -10800000, :rule "-", :format "-03", :from 1033430400000},
                "Europe/Belgrade"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 407203200000},
                "Europe/Kiev"
                {:offset 7200000, :rule "EU", :format "EE%sT", :from 788918400000},
                "Europe/Zagreb" "Europe/Belgrade",
                "Asia/Chungking" "Asia/Shanghai",
                "Canada/Mountain" "America/Edmonton",
                "Pacific/Samoa" "Pacific/Pago_Pago",
                "Africa/Douala" "Africa/Lagos",
                "America/Argentina/La_Rioja"
                {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
                "Europe/Kaliningrad"
                {:offset 7200000, :rule "-", :format "EET", :from 1414288800000},
                "Asia/Gaza"
                {:offset 7200000,
                 :rule "Palestine",
                 :format "EE%sT",
                 :from 1325376000000},
                "US/Alaska" "America/Anchorage",
                "Asia/Yerevan"
                {:offset 14400000,
                 :rule "Armenia",
                 :format "+04/+05",
                 :from 1293840000000},
                "Europe/Warsaw"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 567993600000},
                "Australia/Currie"
                {:offset 36000000, :rule "AT", :format "AE%sT", :from 47174400000},
                "Africa/Nouakchott" "Africa/Abidjan",
                "Egypt" "Africa/Cairo",
                "Africa/Brazzaville" "Africa/Lagos",
                "Asia/Yekaterinburg"
                {:offset 18000000, :rule "-", :format "+05", :from 1414288800000},
                "America/Belize"
                {:offset -21600000,
                 :rule "Belize",
                 :format "%s",
                 :from -1822521600000},
                "Atlantic/Azores"
                {:offset -3600000,
                 :rule "EU",
                 :format "-01/+00",
                 :from 733280400000},
                "Etc/GMT-9" {:offset 32400000, :rule "-", :format "+09", :from nil},
                "America/Argentina/San_Juan"
                {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
                "US/Michigan" "America/Detroit",
                "Asia/Kamchatka"
                {:offset 43200000, :rule "-", :format "+12", :from 1301191200000},
                "America/Knox_IN" "America/Indiana/Knox",
                "Australia/Adelaide"
                {:offset 34200000, :rule "AS", :format "AC%sT", :from 31536000000},
                "MST" {:offset -25200000, :rule "-", :format "MST", :from nil},
                "ROK" "Asia/Seoul",
                "America/Rankin_Inlet"
                {:offset -21600000,
                 :rule "Canada",
                 :format "C%sT",
                 :from 986094000000},
                "Etc/UCT" {:offset 0, :rule "-", :format "UCT", :from nil},
                "Pacific/Kwajalein"
                {:offset 43200000, :rule "-", :format "+12", :from 745804800000},
                "Asia/Yangon"
                {:offset 23400000, :rule "-", :format "+0630", :from -778377600000},
                "Mexico/BajaSur" "America/Mazatlan",
                "Canada/Pacific" "America/Vancouver",
                "America/Tortola" "America/Port_of_Spain",
                "Japan" "Asia/Tokyo",
                "Europe/Volgograd"
                {:offset 10800000, :rule "-", :format "+03", :from 1414288800000},
                "America/Inuvik"
                {:offset -25200000,
                 :rule "Canada",
                 :format "M%sT",
                 :from 315532800000},
                "UCT" "Etc/UCT",
                "Asia/Pyongyang"
                {:offset 32400000, :rule "-", :format "KST", :from 1525478400000},
                "Canada/Central" "America/Winnipeg",
                "Asia/Vientiane" "Asia/Bangkok",
                "Africa/Lubumbashi" "Africa/Maputo",
                "W-SU" "Europe/Moscow",
                "America/Dawson"
                {:offset -28800000,
                 :rule "Canada",
                 :format "P%sT",
                 :from 315532800000},
                "Asia/Ulan_Bator" "Asia/Ulaanbaatar",
                "Asia/Yakutsk"
                {:offset 32400000, :rule "-", :format "+09", :from 1414288800000},
                "Europe/San_Marino" "Europe/Rome",
                "America/Porto_Acre" "America/Rio_Branco",
                "Pacific/Rarotonga"
                {:offset -36000000,
                 :rule "Cook",
                 :format "-10/-0930",
                 :from 279676800000},
                "GB" "Europe/London",
                "America/Curacao"
                {:offset -14400000, :rule "-", :format "AST", :from -157766400000},
                "America/La_Paz"
                {:offset -14400000, :rule "-", :format "-04", :from -1192320000000},
                "Europe/Astrakhan"
                {:offset 14400000, :rule "-", :format "+04", :from 1459044000000},
                "Europe/Prague"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 283996800000},
                "America/Chihuahua"
                {:offset -25200000,
                 :rule "Mexico",
                 :format "M%sT",
                 :from 891745200000},
                "America/Los_Angeles"
                {:offset -28800000, :rule "US", :format "P%sT", :from -94694400000},
                "America/Lima"
                {:offset -18000000,
                 :rule "Peru",
                 :format "-05/-04",
                 :from -1938556800000},
                "America/Caracas"
                {:offset -14400000, :rule "-", :format "-04", :from 1462069800000},
                "Africa/Dakar" "Africa/Abidjan",
                "Kwajalein" "Pacific/Kwajalein",
                "Etc/GMT-14" {:offset 50400000, :rule "-", :format "+14", :from nil},
                "America/Buenos_Aires" "America/Argentina/Buenos_Aires",
                "America/Virgin" "America/Port_of_Spain",
                "Asia/Nicosia"
                {:offset 7200000,
                 :rule "EUAsia",
                 :format "EE%sT",
                 :from 904608000000},
                "America/Indiana/Petersburg"
                {:offset -18000000, :rule "US", :format "E%sT", :from 1194141600000},
                "Etc/GMT-2" {:offset 7200000, :rule "-", :format "+02", :from nil},
                "America/Tijuana"
                {:offset -28800000, :rule "US", :format "P%sT", :from 1262304000000},
                "Europe/Guernsey" "Europe/London",
                "Africa/Conakry" "Africa/Abidjan",
                "Europe/Dublin"
                {:offset 3600000,
                 :rule "Eire",
                 :format "IST/GMT",
                 :from -37238400000},
                "Europe/Helsinki"
                {:offset 7200000, :rule "EU", :format "EE%sT", :from 410227200000},
                "Africa/Abidjan"
                {:offset 0, :rule "-", :format "GMT", :from -1830384000000},
                "America/Campo_Grande"
                {:offset -14400000,
                 :rule "Brazil",
                 :format "-04/-03",
                 :from -1767225600000},
                "America/Guatemala"
                {:offset -21600000,
                 :rule "Guat",
                 :format "C%sT",
                 :from -1617062400000},
                "Pacific/Tongatapu"
                {:offset 46800000,
                 :rule "Tonga",
                 :format "+13/+14",
                 :from 915148800000},
                "Africa/Monrovia"
                {:offset 0, :rule "-", :format "GMT", :from 63590400000},
                "Atlantic/Bermuda"
                {:offset -14400000, :rule "US", :format "A%sT", :from 189302400000},
                "Asia/Almaty"
                {:offset 21600000, :rule "-", :format "+06", :from 1099188000000},
                "Asia/Bishkek"
                {:offset 21600000, :rule "-", :format "+06", :from 1123804800000},
                "Indian/Mayotte" "Africa/Nairobi",
                "America/Martinique"
                {:offset -14400000, :rule "-", :format "AST", :from 338947200000},
                "America/Shiprock" "America/Denver",
                "America/Danmarkshavn"
                {:offset 0, :rule "-", :format "GMT", :from 820454400000},
                "Asia/Tel_Aviv" "Asia/Jerusalem",
                "Europe/Athens"
                {:offset 7200000, :rule "EU", :format "EE%sT", :from 347155200000},
                "Brazil/Acre" "America/Rio_Branco",
                "Europe/Sarajevo" "Europe/Belgrade",
                "EST" {:offset -18000000, :rule "-", :format "EST", :from nil},
                "NZ" "Pacific/Auckland",
                "America/Yellowknife"
                {:offset -25200000,
                 :rule "Canada",
                 :format "M%sT",
                 :from 315532800000},
                "Africa/Freetown" "Africa/Abidjan",
                "Asia/Tbilisi"
                {:offset 14400000, :rule "-", :format "+04", :from 1111888800000},
                "Canada/Yukon" "America/Whitehorse",
                "Pacific/Pago_Pago"
                {:offset -39600000, :rule "-", :format "SST", :from -1861920000000},
                "America/Argentina/Ushuaia"
                {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
                "ROC" "Asia/Taipei",
                "America/Metlakatla"
                {:offset -32400000,
                 :rule "US",
                 :format "AK%sT",
                 :from 1446343200000},
                "America/Boa_Vista"
                {:offset -14400000, :rule "-", :format "-04", :from 971568000000},
                "Pacific/Nauru"
                {:offset 43200000, :rule "-", :format "+12", :from 294364800000},
                "Asia/Brunei"
                {:offset 28800000, :rule "-", :format "+08", :from -1167609600000},
                "Pacific/Palau"
                {:offset 32400000, :rule "-", :format "+09", :from -2177452800000},
                "Asia/Dili"
                {:offset 32400000, :rule "-", :format "+09", :from 969148800000},
                "Etc/GMT+8" {:offset -28800000, :rule "-", :format "-08", :from nil},
                "Etc/Zulu" "Etc/UTC",
                "America/Port-au-Prince"
                {:offset -18000000,
                 :rule "Haiti",
                 :format "E%sT",
                 :from -1670500800000},
                "Pacific/Chatham"
                {:offset 45900000,
                 :rule "Chatham",
                 :format "+1245/+1345",
                 :from -757382400000},
                "America/Indiana/Indianapolis"
                {:offset -18000000, :rule "US", :format "E%sT", :from 1136073600000},
                "Atlantic/Reykjavik"
                {:offset 0, :rule "-", :format "GMT", :from -54774000000},
                "Iran" "Asia/Tehran",
                "America/Kentucky/Louisville"
                {:offset -18000000, :rule "US", :format "E%sT", :from 152071200000},
                "Africa/Asmera" "Africa/Nairobi",
                "Australia/Sydney"
                {:offset 36000000, :rule "AN", :format "AE%sT", :from 31536000000},
                "America/Indiana/Vincennes"
                {:offset -18000000, :rule "US", :format "E%sT", :from 1194141600000},
                "America/Bogota"
                {:offset -18000000,
                 :rule "CO",
                 :format "-05/-04",
                 :from -1739059200000},
                "US/Indiana-Starke" "America/Indiana/Knox",
                "America/Phoenix"
                {:offset -25200000, :rule "-", :format "MST", :from -56246400000},
                "Africa/Djibouti" "Africa/Nairobi",
                "Etc/GMT-3" {:offset 10800000, :rule "-", :format "+03", :from nil},
                "Africa/Banjul" "Africa/Abidjan",
                "Brazil/East" "America/Sao_Paulo",
                "Asia/Atyrau"
                {:offset 18000000, :rule "-", :format "+05", :from 1099188000000},
                "America/Cayenne"
                {:offset -10800000, :rule "-", :format "-03", :from -71107200000},
                "America/Santa_Isabel" "America/Tijuana",
                "America/Grand_Turk"
                {:offset -18000000, :rule "US", :format "E%sT", :from 1520737200000},
                "Europe/Vilnius"
                {:offset 7200000, :rule "EU", :format "EE%sT", :from 1041379200000},
                "America/Hermosillo"
                {:offset -25200000, :rule "-", :format "MST", :from 915148800000},
                "America/Yakutat"
                {:offset -32400000, :rule "US", :format "AK%sT", :from 438998400000},
                "Africa/Kampala" "Africa/Nairobi",
                "Europe/Copenhagen"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 315532800000},
                "Asia/Novosibirsk"
                {:offset 25200000, :rule "-", :format "+07", :from 1469325600000},
                "Europe/Monaco"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 220924800000},
                "Brazil/West" "America/Manaus",
                "EST5EDT" {:offset -18000000, :rule "US", :format "E%sT", :from nil},
                "America/Chicago"
                {:offset -21600000, :rule "US", :format "C%sT", :from -94694400000},
                "Etc/GMT-1" {:offset 3600000, :rule "-", :format "+01", :from nil},
                "America/Godthab"
                {:offset -10800000,
                 :rule "EU",
                 :format "-03/-02",
                 :from 323834400000},
                "Canada/Atlantic" "America/Halifax",
                "Asia/Singapore"
                {:offset 28800000, :rule "-", :format "+08", :from 378691200000},
                "Etc/GMT-8" {:offset 28800000, :rule "-", :format "+08", :from nil},
                "America/Lower_Princes" "America/Curacao",
                "Africa/Accra"
                {:offset 0,
                 :rule "Ghana",
                 :format "GMT/+0020",
                 :from -1640995200000},
                "America/Asuncion"
                {:offset -14400000,
                 :rule "Para",
                 :format "-04/-03",
                 :from 134006400000},
                "America/Thunder_Bay"
                {:offset -18000000,
                 :rule "Canada",
                 :format "E%sT",
                 :from 126230400000},
                "America/Bahia"
                {:offset -10800000, :rule "-", :format "-03", :from 1350777600000},
                "Pacific/Majuro"
                {:offset 43200000, :rule "-", :format "+12", :from -7948800000},
                "Etc/GMT+9" {:offset -32400000, :rule "-", :format "-09", :from nil},
                "Asia/Tomsk"
                {:offset 25200000, :rule "-", :format "+07", :from 1464487200000},
                "Africa/Mbabane" "Africa/Johannesburg",
                "Pacific/Fakaofo"
                {:offset 46800000, :rule "-", :format "+13", :from 1325203200000},
                "Africa/Addis_Ababa" "Africa/Nairobi",
                "America/Nassau"
                {:offset -18000000, :rule "US", :format "E%sT", :from 189302400000},
                "Asia/Makassar"
                {:offset 28800000, :rule "-", :format "WITA", :from -766022400000},
                "Europe/Nicosia" "Asia/Nicosia",
                "Australia/West" "Australia/Perth",
                "America/Whitehorse"
                {:offset -28800000,
                 :rule "Canada",
                 :format "P%sT",
                 :from 315532800000},
                "Asia/Urumqi"
                {:offset 21600000, :rule "-", :format "+06", :from -1325462400000},
                "Asia/Famagusta"
                {:offset 7200000,
                 :rule "EUAsia",
                 :format "EE%sT",
                 :from 1509238800000},
                "US/Samoa" "Pacific/Pago_Pago",
                "Asia/Samarkand"
                {:offset 18000000, :rule "-", :format "+05", :from 694224000000},
                "Asia/Calcutta" "Asia/Kolkata",
                "Pacific/Niue"
                {:offset -39600000, :rule "-", :format "-11", :from 276048000000},
                "Australia/Perth"
                {:offset 28800000, :rule "AW", :format "AW%sT", :from -836438400000},
                "Africa/Bissau"
                {:offset 0, :rule "-", :format "GMT", :from 157766400000},
                "Asia/Thimphu"
                {:offset 21600000, :rule "-", :format "+06", :from 560044800000},
                "America/Antigua" "America/Port_of_Spain",
                "Africa/Khartoum"
                {:offset 7200000, :rule "-", :format "CAT", :from 1509494400000},
                "America/Guyana"
                {:offset -14400000, :rule "-", :format "-04", :from 662688000000},
                "America/Puerto_Rico"
                {:offset -14400000, :rule "-", :format "AST", :from -757382400000},
                "Pacific/Apia"
                {:offset 46800000,
                 :rule "WS",
                 :format "+13/+14",
                 :from 1325203200000},
                "America/Indiana/Winamac"
                {:offset -18000000, :rule "US", :format "E%sT", :from 1173578400000},
                "US/East-Indiana" "America/Indiana/Indianapolis",
                "Pacific/Galapagos"
                {:offset -21600000,
                 :rule "Ecuador",
                 :format "-06/-05",
                 :from 504921600000},
                "Europe/Skopje" "Europe/Belgrade",
                "GB-Eire" "Europe/London",
                "Europe/Istanbul"
                {:offset 10800000, :rule "-", :format "+03", :from 1473206400000},
                "Chile/Continental" "America/Santiago",
                "Asia/Jerusalem"
                {:offset 7200000,
                 :rule "Zion",
                 :format "I%sT",
                 :from -1640995200000},
                "Asia/Magadan"
                {:offset 39600000, :rule "-", :format "+11", :from 1461463200000},
                "UTC" "Etc/UTC",
                "America/North_Dakota/Beulah"
                {:offset -21600000, :rule "US", :format "C%sT", :from 1289095200000},
                "America/Guadeloupe" "America/Port_of_Spain",
                "America/St_Lucia" "America/Port_of_Spain",
                "America/Mexico_City"
                {:offset -21600000,
                 :rule "Mexico",
                 :format "C%sT",
                 :from 1014163200000},
                "Africa/Johannesburg"
                {:offset 7200000, :rule "SA", :format "SAST", :from -2109283200000},
                "Africa/Bamako" "Africa/Abidjan",
                "Etc/GMT+0" "Etc/GMT",
                "America/Toronto"
                {:offset -18000000,
                 :rule "Canada",
                 :format "E%sT",
                 :from 126230400000},
                "Etc/GMT-7" {:offset 25200000, :rule "-", :format "+07", :from nil},
                "America/Managua"
                {:offset -21600000, :rule "Nic", :format "C%sT", :from 852076800000},
                "America/Belem"
                {:offset -10800000, :rule "-", :format "-03", :from 590025600000},
                "Europe/Lisbon"
                {:offset 0, :rule "EU", :format "WE%sT", :from 828234000000},
                "Asia/Kashgar" "Asia/Urumqi",
                "America/Juneau"
                {:offset -32400000, :rule "US", :format "AK%sT", :from 438998400000},
                "America/Fort_Wayne" "America/Indiana/Indianapolis",
                "US/Mountain" "America/Denver",
                "Etc/GMT+6" {:offset -21600000, :rule "-", :format "-06", :from nil},
                "Europe/Minsk"
                {:offset 10800000, :rule "-", :format "+03", :from 1301191200000},
                "America/Indiana/Tell_City"
                {:offset -21600000, :rule "US", :format "C%sT", :from 1143943200000},
                "Australia/North" "Australia/Darwin",
                "Europe/Moscow"
                {:offset 10800000, :rule "-", :format "MSK", :from 1414288800000},
                "Pacific/Kosrae"
                {:offset 39600000, :rule "-", :format "+11", :from 915148800000},
                "Europe/Ulyanovsk"
                {:offset 14400000, :rule "-", :format "+04", :from 1459044000000},
                "Africa/Lagos"
                {:offset 3600000, :rule "-", :format "WAT", :from -1588464000000},
                "Asia/Harbin" "Asia/Shanghai",
                "Iceland" "Atlantic/Reykjavik",
                "Mexico/General" "America/Mexico_City",
                "America/Guayaquil"
                {:offset -18000000,
                 :rule "Ecuador",
                 :format "-05/-04",
                 :from -1230768000000},
                "America/Argentina/Cordoba"
                {:offset -10800000,
                 :rule "Arg",
                 :format "-03/-02",
                 :from 952041600000},
                "Africa/Windhoek"
                {:offset 7200000, :rule "Namibia", :format "%s", :from 637977600000},
                "Europe/Rome"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 315532800000},
                "Asia/Krasnoyarsk"
                {:offset 25200000, :rule "-", :format "+07", :from 1414288800000},
                "America/Glace_Bay"
                {:offset -14400000,
                 :rule "Canada",
                 :format "A%sT",
                 :from 126230400000},
                "Asia/Karachi"
                {:offset 18000000,
                 :rule "Pakistan",
                 :format "PK%sT",
                 :from 38793600000},
                "Europe/Busingen" "Europe/Zurich",
                "Asia/Aden" "Asia/Riyadh",
                "America/Mazatlan"
                {:offset -25200000, :rule "Mexico", :format "M%sT", :from 0},
                "America/Winnipeg"
                {:offset -21600000,
                 :rule "Canada",
                 :format "C%sT",
                 :from 1136073600000},
                "Europe/Chisinau"
                {:offset 7200000,
                 :rule "Moldova",
                 :format "EE%sT",
                 :from 852076800000},
                "America/Argentina/Buenos_Aires"
                {:offset -10800000,
                 :rule "Arg",
                 :format "-03/-02",
                 :from 952041600000},
                "Europe/Riga"
                {:offset 7200000, :rule "EU", :format "EE%sT", :from 978393600000},
                "Pacific/Pitcairn"
                {:offset -28800000, :rule "-", :format "-08", :from 893635200000},
                "Turkey" "Europe/Istanbul",
                "Pacific/Guam"
                {:offset 36000000, :rule "-", :format "ChST", :from 977529600000},
                "Indian/Reunion"
                {:offset 14400000, :rule "-", :format "+04", :from -1848873600000},
                "Pacific/Bougainville"
                {:offset 39600000, :rule "-", :format "+11", :from 1419732000000},
                "Africa/Malabo" "Africa/Lagos",
                "America/New_York"
                {:offset -18000000, :rule "US", :format "E%sT", :from -94694400000},
                "America/Coral_Harbour" "America/Atikokan",
                "Africa/Tripoli"
                {:offset 7200000, :rule "-", :format "EET", :from 1382666400000},
                "Asia/Bangkok"
                {:offset 25200000, :rule "-", :format "+07", :from -1570060800000},
                "America/Atikokan"
                {:offset -18000000, :rule "-", :format "EST", :from -765410400000},
                "America/Detroit"
                {:offset -18000000, :rule "US", :format "E%sT", :from 167796000000},
                "Poland" "Europe/Warsaw",
                "Etc/GMT+11"
                {:offset -39600000, :rule "-", :format "-11", :from nil},
                "Asia/Ashgabat"
                {:offset 18000000, :rule "-", :format "+05", :from 695786400000},
                "America/Edmonton"
                {:offset -25200000,
                 :rule "Canada",
                 :format "M%sT",
                 :from 536457600000},
                "Europe/Podgorica" "Europe/Belgrade",
                "Europe/Saratov"
                {:offset 14400000, :rule "-", :format "+04", :from 1480816800000},
                "Greenwich" "Etc/GMT",
                "Australia/ACT" "Australia/Sydney",
                "Asia/Damascus"
                {:offset 7200000,
                 :rule "Syria",
                 :format "EE%sT",
                 :from -1577923200000},
                "Africa/Bujumbura" "Africa/Maputo",
                "Australia/Lord_Howe"
                {:offset 37800000,
                 :rule "LH",
                 :format "+1030/+11",
                 :from 489024000000},
                "Singapore" "Asia/Singapore",
                "Africa/Bangui" "Africa/Lagos",
                "America/Kentucky/Monticello"
                {:offset -18000000, :rule "US", :format "E%sT", :from 972784800000},
                "Europe/Mariehamn" "Europe/Helsinki",
                "Australia/Eucla"
                {:offset 31500000,
                 :rule "AW",
                 :format "+0845/+0945",
                 :from -836438400000},
                "Canada/Newfoundland" "America/St_Johns",
                "America/North_Dakota/New_Salem"
                {:offset -21600000, :rule "US", :format "C%sT", :from 1067133600000},
                "Africa/Juba"
                {:offset 10800000, :rule "-", :format "EAT", :from 947937600000},
                "Africa/Gaborone" "Africa/Maputo",
                "Etc/GMT-5" {:offset 18000000, :rule "-", :format "+05", :from nil},
                "Canada/Saskatchewan" "America/Regina",
                "Europe/Ljubljana" "Europe/Belgrade",
                "Australia/Lindeman"
                {:offset 36000000,
                 :rule "Holiday",
                 :format "AE%sT",
                 :from 709948800000},
                "Africa/Mogadishu" "Africa/Nairobi",
                "America/St_Thomas" "America/Port_of_Spain",
                "America/Havana"
                {:offset -18000000,
                 :rule "Cuba",
                 :format "C%sT",
                 :from -1402833600000},
                "Asia/Ulaanbaatar"
                {:offset 28800000,
                 :rule "Mongol",
                 :format "+08/+09",
                 :from 252460800000},
                "Europe/Vaduz" "Europe/Zurich",
                "America/Anchorage"
                {:offset -32400000, :rule "US", :format "AK%sT", :from 438998400000},
                "America/Iqaluit"
                {:offset -18000000,
                 :rule "Canada",
                 :format "E%sT",
                 :from 972784800000},
                "Asia/Riyadh"
                {:offset 10800000, :rule "-", :format "+03", :from -719625600000},
                "America/Vancouver"
                {:offset -28800000,
                 :rule "Canada",
                 :format "P%sT",
                 :from 536457600000},
                "US/Pacific" "America/Los_Angeles",
                "America/Grenada" "America/Port_of_Spain",
                "America/Rio_Branco"
                {:offset -18000000, :rule "-", :format "-05", :from 1384041600000},
                "Europe/Bratislava" "Europe/Prague",
                "Pacific/Fiji"
                {:offset 43200000,
                 :rule "Fiji",
                 :format "+12/+13",
                 :from -1709942400000},
                "Asia/Omsk"
                {:offset 21600000, :rule "-", :format "+06", :from 1414288800000},
                "America/Catamarca" "America/Argentina/Catamarca",
                "Pacific/Funafuti"
                {:offset 43200000, :rule "-", :format "+12", :from -2177452800000},
                "America/Merida"
                {:offset -21600000,
                 :rule "Mexico",
                 :format "C%sT",
                 :from 407635200000},
                "Asia/Istanbul" "Europe/Istanbul",
                "America/Cayman" "America/Panama",
                "Etc/GMT+7" {:offset -25200000, :rule "-", :format "-07", :from nil},
                "Etc/GMT-6" {:offset 21600000, :rule "-", :format "+06", :from nil},
                "Zulu" "Etc/UTC",
                "Pacific/Noumea"
                {:offset 39600000,
                 :rule "NC",
                 :format "+11/+12",
                 :from -1829347200000},
                "Indian/Mahe"
                {:offset 14400000, :rule "-", :format "+04", :from -2006640000000},
                "Asia/Irkutsk"
                {:offset 28800000, :rule "-", :format "+08", :from 1414288800000},
                "Antarctica/Palmer"
                {:offset -10800000, :rule "-", :format "-03", :from 1480809600000},
                "America/Bahia_Banderas"
                {:offset -21600000,
                 :rule "Mexico",
                 :format "C%sT",
                 :from 1270346400000},
                "Europe/Budapest"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 338954400000},
                "America/Regina"
                {:offset -21600000, :rule "-", :format "CST", :from -305762400000},
                "America/Halifax"
                {:offset -14400000,
                 :rule "Canada",
                 :format "A%sT",
                 :from 126230400000},
                "Asia/Anadyr"
                {:offset 43200000, :rule "-", :format "+12", :from 1301191200000},
                "America/Cuiaba"
                {:offset -14400000,
                 :rule "Brazil",
                 :format "-04/-03",
                 :from 1096588800000},
                "Australia/NSW" "Australia/Sydney",
                "Africa/Porto-Novo" "Africa/Lagos",
                "America/Argentina/Catamarca"
                {:offset -10800000, :rule "-", :format "-03", :from 1224288000000},
                "Indian/Mauritius"
                {:offset 14400000,
                 :rule "Mauritius",
                 :format "+04/+05",
                 :from -1988150400000},
                "Europe/Samara"
                {:offset 14400000, :rule "-", :format "+04", :from 1301191200000},
                "America/Eirunepe"
                {:offset -18000000, :rule "-", :format "-05", :from 1384041600000},
                "America/Fort_Nelson"
                {:offset -25200000, :rule "-", :format "MST", :from 1425780000000},
                "Atlantic/Faeroe" "Atlantic/Faroe",
                "Africa/Asmara" "Africa/Nairobi",
                "Asia/Tashkent"
                {:offset 18000000, :rule "-", :format "+05", :from 694224000000},
                "Etc/GMT-11" {:offset 39600000, :rule "-", :format "+11", :from nil},
                "Africa/Maputo"
                {:offset 7200000, :rule "-", :format "CAT", :from -2109283200000},
                "Pacific/Kiritimati"
                {:offset 50400000, :rule "-", :format "+14", :from 788832000000},
                "PRC" "Asia/Shanghai",
                "America/Argentina/ComodRivadavia" "America/Argentina/Catamarca",
                "Asia/Qyzylorda"
                {:offset 21600000, :rule "-", :format "+06", :from 1099188000000},
                "Pacific/Midway" "Pacific/Pago_Pago",
                "America/Kralendijk" "America/Curacao",
                "Asia/Kabul"
                {:offset 16200000, :rule "-", :format "+0430", :from -788918400000},
                "America/Port_of_Spain"
                {:offset -14400000, :rule "-", :format "AST", :from -1825113600000},
                "Europe/Zurich"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 347155200000},
                "America/Indianapolis" "America/Indiana/Indianapolis",
                "NZ-CHAT" "Pacific/Chatham",
                "Atlantic/Jan_Mayen" "Europe/Oslo",
                "Asia/Kathmandu"
                {:offset 20700000, :rule "-", :format "+0545", :from 504921600000},
                "Africa/Algiers"
                {:offset 3600000, :rule "-", :format "CET", :from 357523200000},
                "America/Matamoros"
                {:offset -21600000, :rule "US", :format "C%sT", :from 1262304000000},
                "America/Araguaina"
                {:offset -10800000, :rule "-", :format "-03", :from 1377993600000},
                "America/Rosario" "America/Argentina/Cordoba",
                "Etc/Greenwich" "Etc/GMT",
                "America/Argentina/Tucuman"
                {:offset -10800000,
                 :rule "Arg",
                 :format "-03/-02",
                 :from 1087084800000},
                "Antarctica/South_Pole" "Pacific/Auckland",
                "America/Marigot" "America/Port_of_Spain",
                "Pacific/Marquesas"
                {:offset -34200000,
                 :rule "-",
                 :format "-0930",
                 :from -1806710400000},
                "Pacific/Truk" "Pacific/Chuuk",
                "Asia/Baghdad"
                {:offset 10800000,
                 :rule "Iraq",
                 :format "+03/+04",
                 :from 389059200000},
                "America/Dawson_Creek"
                {:offset -25200000, :rule "-", :format "MST", :from 83988000000},
                "Europe/Kirov"
                {:offset 10800000, :rule "-", :format "+03", :from 1414288800000},
                "America/Santo_Domingo"
                {:offset -14400000, :rule "-", :format "AST", :from 975805200000},
                "US/Central" "America/Chicago",
                "America/Miquelon"
                {:offset -10800000,
                 :rule "Canada",
                 :format "-03/-02",
                 :from 536457600000},
                "Pacific/Wake"
                {:offset 43200000, :rule "-", :format "+12", :from -2177452800000},
                "Asia/Ust-Nera"
                {:offset 36000000, :rule "-", :format "+10", :from 1414288800000},
                "Australia/Canberra" "Australia/Sydney",
                "Indian/Antananarivo" "Africa/Nairobi",
                "Brazil/DeNoronha" "America/Noronha",
                "Europe/Malta"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 347155200000},
                "Europe/Paris"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 220924800000},
                "Africa/Luanda" "Africa/Lagos",
                "Europe/Madrid"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 283996800000},
                "Asia/Chongqing" "Asia/Shanghai",
                "Europe/Gibraltar"
                {:offset 3600000, :rule "EU", :format "CE%sT", :from 378691200000},
                "America/Cancun"
                {:offset -18000000, :rule "-", :format "EST", :from 1422756000000},
                "Asia/Dacca" "Asia/Dhaka",
                "Israel" "Asia/Jerusalem",
                "Pacific/Port_Moresby"
                {:offset 36000000, :rule "-", :format "+10", :from -2366755200000},
                "Atlantic/Cape_Verde"
                {:offset -3600000, :rule "-", :format "-01", :from 186112800000},
                "Australia/Darwin"
                {:offset 34200000,
                 :rule "Aus",
                 :format "AC%sT",
                 :from -2230156800000},
                "Africa/Nairobi"
                {:offset 10800000, :rule "-", :format "EAT", :from -315619200000},
                "WET" {:offset 0, :rule "EU", :format "WE%sT", :from nil},
                "Atlantic/Canary"
                {:offset 0, :rule "EU", :format "WE%sT", :from 338950800000},
                "Africa/Libreville" "Africa/Lagos",
                "America/Paramaribo"
                {:offset -10800000, :rule "-", :format "-03", :from 465436800000},
                "Asia/Ho_Chi_Minh"
                {:offset 25200000, :rule "-", :format "+07", :from 171849600000},
                "Europe/Vatican" "Europe/Rome",
                "Africa/Ndjamena"
                {:offset 3600000, :rule "-", :format "WAT", :from 321321600000},
                "Australia/Melbourne"
                {:offset 36000000, :rule "AV", :format "AE%sT", :from 31536000000},
                "America/Manaus"
                {:offset -14400000, :rule "-", :format "-04", :from 780192000000},
                "Etc/GMT-13" {:offset 46800000, :rule "-", :format "+13", :from nil},
                "America/Adak"
                {:offset -36000000, :rule "US", :format "H%sT", :from 438998400000},
                "Asia/Taipei"
                {:offset 28800000,
                 :rule "Taiwan",
                 :format "C%sT",
                 :from -766191600000},
                "Asia/Dhaka"
                {:offset 21600000,
                 :rule "Dhaka",
                 :format "+06/+07",
                 :from 1230768000000}},
               :rules
               {"CR" nil,
                "Pakistan" nil,
                "Nic" nil,
                "Perry" nil,
                "Tunisia" nil,
                "Uruguay" nil,
                "Denver" nil,
                "Hungary" nil,
                "Cuba"
                {:standard
                 {:from 1351987200000,
                  :clock? :standard,
                  :floating-day "Sun>=1",
                  :time {:hour 0, :minute 0, :time-suffix "s"},
                  :month 11,
                  :save 0},
                 :daylight-savings
                 {:from 1362873600000,
                  :clock? :standard,
                  :floating-day "Sun>=8",
                  :time {:hour 0, :minute 0, :time-suffix "s"},
                  :month 3,
                  :save 3600000}},
                "E-Eur"
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
                "Haiti"
                {:daylight-savings
                 {:from 1489284000000,
                  :clock? :utc,
                  :floating-day "Sun>=8",
                  :time {:hour 2, :minute 0},
                  :month 3,
                  :save 3600000},
                 :standard
                 {:from 1509847200000,
                  :clock? :utc,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0},
                  :month 11,
                  :save 0}},
                "Chicago" nil,
                "Shang" nil,
                "NT_YK" nil,
                "Bulg" nil,
                "Salv" nil,
                "Moldova"
                {:daylight-savings
                 {:from 859687200000,
                  :clock? :utc,
                  :floating-day "lastSun",
                  :time {:hour 2, :minute 0},
                  :month 3,
                  :save 3600000},
                 :standard
                 {:from 877834800000,
                  :clock? :utc,
                  :floating-day "lastSun",
                  :time {:hour 3, :minute 0},
                  :month 10,
                  :save 0}},
                "Winn" nil,
                "Chile"
                {:standard
                 {:from 1463281200000,
                  :clock? :utc,
                  :floating-day "Sun>=9",
                  :time {:hour 3, :minute 0, :time-suffix "u"},
                  :month 5,
                  :save 0},
                 :daylight-savings
                 {:from 1471147200000,
                  :clock? :utc,
                  :floating-day "Sun>=9",
                  :time {:hour 4, :minute 0, :time-suffix "u"},
                  :month 8,
                  :save 3600000}},
                "SA" nil,
                "Aus" nil,
                "Brazil"
                {:daylight-savings
                 {:from 1541289600000,
                  :clock? :utc,
                  :floating-day "Sun>=1",
                  :time {:hour 0, :minute 0},
                  :month 11,
                  :save 3600000},
                 :standard
                 {:from 2150323200000,
                  :clock? :utc,
                  :floating-day "Sun>=15",
                  :time {:hour 0, :minute 0},
                  :month 2,
                  :save 0}},
                "EUAsia"
                {:daylight-savings
                 {:from 354675600000,
                  :clock? :utc,
                  :floating-day "lastSun",
                  :time {:hour 1, :minute 0, :time-suffix "u"},
                  :month 3,
                  :save 3600000},
                 :standard
                 {:from 846378000000,
                  :clock? :utc,
                  :floating-day "lastSun",
                  :time {:hour 1, :minute 0, :time-suffix "u"},
                  :month 10,
                  :save 0}},
                "Hond" nil,
                "SpainAfrica" nil,
                "Sudan" nil,
                "EU"
                {:daylight-savings
                 {:from 354675600000,
                  :clock? :utc,
                  :floating-day "lastSun",
                  :time {:hour 1, :minute 0, :time-suffix "u"},
                  :month 3,
                  :save 3600000},
                 :standard
                 {:from 846378000000,
                  :clock? :utc,
                  :floating-day "lastSun",
                  :time {:hour 1, :minute 0, :time-suffix "u"},
                  :month 10,
                  :save 0}},
                "Pulaski" nil,
                "Arg" nil,
                "Palestine"
                {:daylight-savings
                 {:from 1458954000000,
                  :clock? :utc,
                  :floating-day "Sat>=22",
                  :time {:hour 1, :minute 0},
                  :month 3,
                  :save 3600000},
                 :standard
                 {:from 1477702800000,
                  :clock? :utc,
                  :floating-day "lastSat",
                  :time {:hour 1, :minute 0},
                  :month 10,
                  :save 0}},
                "Eire"
                {:standard
                 {:from 354675600000,
                  :clock? :utc,
                  :floating-day "lastSun",
                  :time {:hour 1, :minute 0, :time-suffix "u"},
                  :month 3,
                  :save 0},
                 :daylight-savings
                 {:from 846378000000,
                  :clock? :utc,
                  :floating-day "lastSun",
                  :time {:hour 1, :minute 0, :time-suffix "u"},
                  :month 10,
                  :save -3600000}},
                "Iraq" nil,
                "Canada"
                {:daylight-savings
                 {:from 1173578400000,
                  :clock? :utc,
                  :floating-day "Sun>=8",
                  :time {:hour 2, :minute 0},
                  :month 3,
                  :save 3600000},
                 :standard
                 {:from 1194141600000,
                  :clock? :utc,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0},
                  :month 11,
                  :save 0}},
                "Belgium" nil,
                "Louisville" nil,
                "Mexico"
                {:daylight-savings
                 {:from 1018144800000,
                  :clock? :utc,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0},
                  :month 4,
                  :save 3600000},
                 :standard
                 {:from 1035684000000,
                  :clock? :utc,
                  :floating-day "lastSun",
                  :time {:hour 2, :minute 0},
                  :month 10,
                  :save 0}},
                "Libya" nil,
                "W-Eur"
                {:daylight-savings
                 {:from 354675600000,
                  :clock? :standard,
                  :floating-day "lastSun",
                  :time {:hour 1, :minute 0, :time-suffix "s"},
                  :month 3,
                  :save 3600000},
                 :standard
                 {:from 846378000000,
                  :clock? :standard,
                  :floating-day "lastSun",
                  :time {:hour 1, :minute 0, :time-suffix "s"},
                  :month 10,
                  :save 0}},
                "Moncton" nil,
                "Detroit" nil,
                "Romania" nil,
                "SanLuis" nil,
                "AT"
                {:daylight-savings
                 {:from 1002420000000,
                  :clock? :standard,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0, :time-suffix "s"},
                  :month 10,
                  :save 3600000},
                 :standard
                 {:from 1207447200000,
                  :clock? :standard,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0, :time-suffix "s"},
                  :month 4,
                  :save 0}},
                "Malta" nil,
                "Fiji"
                {:daylight-savings
                 {:from 1414893600000,
                  :clock? :utc,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0},
                  :month 11,
                  :save 3600000},
                 :standard
                 {:from 1421550000000,
                  :clock? :utc,
                  :floating-day "Sun>=14",
                  :time {:hour 3, :minute 0},
                  :month 1,
                  :save 0}},
                "StJohns" nil,
                "Egypt" nil,
                "ROK" nil,
                "Syria"
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
                "Regina" nil,
                "Japan" nil,
                "AN"
                {:standard
                 {:from 1207447200000,
                  :clock? :standard,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0, :time-suffix "s"},
                  :month 4,
                  :save 0},
                 :daylight-savings
                 {:from 1223172000000,
                  :clock? :standard,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0, :time-suffix "s"},
                  :month 10,
                  :save 3600000}},
                "Ecuador" nil,
                "Zion"
                {:daylight-savings
                 {:from 1364522400000,
                  :clock? :utc,
                  :floating-day "Fri>=23",
                  :time {:hour 2, :minute 0},
                  :month 3,
                  :save 3600000},
                 :standard
                 {:from 1382839200000,
                  :clock? :utc,
                  :floating-day "lastSun",
                  :time {:hour 2, :minute 0},
                  :month 10,
                  :save 0}},
                "Ghana" nil,
                "Latvia" nil,
                "Vincennes" nil,
                "Namibia" nil,
                "Tonga" nil,
                "Marengo" nil,
                "Greece" nil,
                "Toronto" nil,
                "Swift" nil,
                "Vanc" nil,
                "Russia" nil,
                "Italy" nil,
                "Belize" nil,
                "France" nil,
                "NZ"
                {:daylight-savings
                 {:from 1191117600000,
                  :clock? :standard,
                  :floating-day "lastSun",
                  :time {:hour 2, :minute 0, :time-suffix "s"},
                  :month 9,
                  :save 3600000},
                 :standard
                 {:from 1207447200000,
                  :clock? :standard,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0, :time-suffix "s"},
                  :month 4,
                  :save 0}},
                "AW" nil,
                "Iran"
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
                "Port" nil,
                "Armenia" nil,
                "WS"
                {:standard
                 {:from 1333252800000,
                  :clock? :utc,
                  :floating-day "Sun>=1",
                  :time {:hour 4, :minute 0},
                  :month 4,
                  :save 0},
                 :daylight-savings
                 {:from 1348974000000,
                  :clock? :utc,
                  :floating-day "lastSun",
                  :time {:hour 3, :minute 0},
                  :month 9,
                  :save 1}},
                "Norway" nil,
                "Algeria" nil,
                "Czech" nil,
                "Lux" nil,
                "Taiwan" nil,
                "Albania" nil,
                "Chatham"
                {:daylight-savings
                 {:from 1191120300000,
                  :clock? :standard,
                  :floating-day "lastSun",
                  :time {:hour 2, :minute 45, :time-suffix "s"},
                  :month 9,
                  :save 3600000},
                 :standard
                 {:from 1207449900000,
                  :clock? :standard,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 45, :time-suffix "s"},
                  :month 4,
                  :save 0}},
                "CA" nil,
                "Cyprus" nil,
                "AV"
                {:standard
                 {:from 1207447200000,
                  :clock? :standard,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0, :time-suffix "s"},
                  :month 4,
                  :save 0},
                 :daylight-savings
                 {:from 1223172000000,
                  :clock? :standard,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0, :time-suffix "s"},
                  :month 10,
                  :save 3600000}},
                "Dhaka" nil,
                "Pike" nil,
                "Finland" nil,
                "Para"
                {:daylight-savings
                 {:from 1286064000000,
                  :clock? :utc,
                  :floating-day "Sun>=1",
                  :time {:hour 0, :minute 0},
                  :month 10,
                  :save 3600000},
                 :standard
                 {:from 1364083200000,
                  :clock? :utc,
                  :floating-day "Sun>=22",
                  :time {:hour 0, :minute 0},
                  :month 3,
                  :save 0}},
                "GB-Eire" nil,
                "Kyrgyz" nil,
                "Denmark" nil,
                "AS"
                {:standard
                 {:from 1207447200000,
                  :clock? :standard,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0, :time-suffix "s"},
                  :month 4,
                  :save 0},
                 :daylight-savings
                 {:from 1223172000000,
                  :clock? :standard,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0, :time-suffix "s"},
                  :month 10,
                  :save 3600000}},
                "Peru" nil,
                "RussiaAsia" nil,
                "Neth" nil,
                "Iceland" nil,
                "NBorneo" nil,
                "Falk" nil,
                "Spain" nil,
                "Germany" nil,
                "Azer" nil,
                "Turkey" nil,
                "Starke" nil,
                "AQ" nil,
                "Poland" nil,
                "NYC" nil,
                "E-EurAsia"
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
                "Bahamas" nil,
                "Phil" nil,
                "US"
                {:daylight-savings
                 {:from 1173578400000,
                  :clock? :utc,
                  :floating-day "Sun>=8",
                  :time {:hour 2, :minute 0},
                  :month 3,
                  :save 3600000},
                 :standard
                 {:from 1194141600000,
                  :clock? :utc,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0},
                  :month 11,
                  :save 0}},
                "Mauritius" nil,
                "EgyptAsia" nil,
                "NC" nil,
                "Macau" nil,
                "Morocco"
                {:standard
                 {:from 1382842800000,
                  :clock? :utc,
                  :floating-day "lastSun",
                  :time {:hour 3, :minute 0},
                  :month 10,
                  :save 0},
                 :daylight-savings
                 {:from 1774749600000,
                  :clock? :utc,
                  :floating-day "lastSun",
                  :time {:hour 2, :minute 0},
                  :month 3,
                  :save 3600000}},
                "Indianapolis" nil,
                "LH"
                {:standard
                 {:from 1207447200000,
                  :clock? :utc,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0},
                  :month 4,
                  :save 0},
                 :daylight-savings
                 {:from 1223172000000,
                  :clock? :utc,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0},
                  :month 10,
                  :save 1800000}},
                "Austria" nil,
                "C-Eur"
                {:daylight-savings
                 {:from 354679200000,
                  :clock? :standard,
                  :floating-day "lastSun",
                  :time {:hour 2, :minute 0, :time-suffix "s"},
                  :month 3,
                  :save 3600000},
                 :standard
                 {:from 846381600000,
                  :clock? :standard,
                  :floating-day "lastSun",
                  :time {:hour 2, :minute 0, :time-suffix "s"},
                  :month 10,
                  :save 0}},
                "Holiday" nil,
                "Barb" nil,
                "Jordan"
                {:daylight-savings
                 {:from 1395964800000,
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
                "PRC" nil,
                "HK" nil,
                "Menominee" nil,
                "Guat" nil,
                "CO" nil,
                "Lebanon"
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
                "Swiss" nil,
                "Halifax" nil,
                "Cook" nil,
                "Mongol" nil,
                "SovietZone" nil,
                "Thule"
                {:daylight-savings
                 {:from 1173578400000,
                  :clock? :utc,
                  :floating-day "Sun>=8",
                  :time {:hour 2, :minute 0},
                  :month 3,
                  :save 3600000},
                 :standard
                 {:from 1194141600000,
                  :clock? :utc,
                  :floating-day "Sun>=1",
                  :time {:hour 2, :minute 0},
                  :month 11,
                  :save 0}},
                "Vanuatu" nil,
                "DR" nil,
                "Edm" nil}}))
