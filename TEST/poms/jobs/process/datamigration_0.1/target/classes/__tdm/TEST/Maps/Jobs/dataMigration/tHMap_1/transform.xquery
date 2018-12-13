declare namespace Map = "urn:oaklandsw:rep:map";
declare namespace com_oaklandsw_transform_runtime_RuntimeSupport = "java:com.oaklandsw.transform.runtime.RuntimeSupport";
declare namespace org_talend_transform_runtime_common_RuntimeMaskingSupport = "java:org.talend.transform.runtime.common.RuntimeMaskingSupport";
declare namespace com_oaklandsw_transform_rep_db_RuntimeSupportDatabase = "java:com.oaklandsw.transform.rep.db.RuntimeSupportDatabase";


(: CG context elem  out: out$/root/row in: in$/root/row loop: $out_Map_root_Map_row inScope?: true :)
declare function local:out_Map_root_Map_row($top as node(),$out_Map_root,$out_Map_root_i)
{
(: seq in$/root/row :)
  for $out_Map_root_Map_row at $out_Map_root_Map_row_i in let $inVar := $out_Map_root/Map:row return  $inVar 
 return (: for :)  (
(: element out$/root/row :) element Map:row { 
  (: optLeaf out$/root/row/po_number :) let $opt := (
(: value out$/root/row/po_number :)
let $unconvertedValue := (
  (: missingCheck :) if (fn:count($out_Map_root_Map_row/Map:po_no[1]) = 0) then ('_789odtMissing') else (
  (: nullCheck :) if ($out_Map_root_Map_row/Map:po_no[1]/@xsi:nil['true']) then ('_osdt987Null') else ($out_Map_root_Map_row/Map:po_no[1]/text()) (: missingCheck :)) (: nullCheck :) ) return if ( $unconvertedValue[1] cast as xs:string? = '_osdt987Null') then () else  text { $unconvertedValue } 
  )
 (: optLeaf cond out$/root/row/po_number :) return if ($opt[last()] cast as xs:string? = '_789odtMissing') then () else if (fn:string-length(fn:string-join($opt[last()],'')) > 0) then 
(: element out$/root/row/po_number :) element Map:po_number { $opt
(: element end  out$/root/row/po_number :) }
  else ()  (: optLeaf cond end out$/root/row/po_number :) , 
  (: optLeaf out$/root/row/sup_id :) let $opt := (
(: value out$/root/row/sup_id :)
let $unconvertedValue := (
  (: missingCheck :) if (fn:count($out_Map_root_Map_row/Map:sup_id[1]) = 0) then ('_789odtMissing') else (
  (: nullCheck :) if ($out_Map_root_Map_row/Map:sup_id[1]/@xsi:nil['true']) then (0) else ($out_Map_root_Map_row/Map:sup_id[1]/text()) (: missingCheck :)) (: nullCheck :) ) return if ( $unconvertedValue[1] cast as xs:string? = '_osdt987Null') then () else  text { $unconvertedValue } 
  )
 (: optLeaf cond out$/root/row/sup_id :) return if ($opt[last()] cast as xs:string? = '_789odtMissing') then () else if (fn:string-length(fn:string-join($opt[last()],'')) > 0) then 
(: element out$/root/row/sup_id :) element Map:sup_id { $opt
(: element end  out$/root/row/sup_id :) }
  else ()  (: optLeaf cond end out$/root/row/sup_id :) , 
  (: optLeaf out$/root/row/ship_to :) let $opt := (
(: value out$/root/row/ship_to :)
let $unconvertedValue := (
  (: missingCheck :) if (fn:count($out_Map_root_Map_row/Map:ship_to[1]) = 0) then ('_789odtMissing') else (
  (: nullCheck :) if ($out_Map_root_Map_row/Map:ship_to[1]/@xsi:nil['true']) then ('_osdt987Null') else ($out_Map_root_Map_row/Map:ship_to[1]/text()) (: missingCheck :)) (: nullCheck :) ) return if ( $unconvertedValue[1] cast as xs:string? = '_osdt987Null') then () else  text { $unconvertedValue } 
  )
 (: optLeaf cond out$/root/row/ship_to :) return if ($opt[last()] cast as xs:string? = '_789odtMissing') then () else if (fn:string-length(fn:string-join($opt[last()],'')) > 0) then 
(: element out$/root/row/ship_to :) element Map:ship_to { $opt
(: element end  out$/root/row/ship_to :) }
  else ()  (: optLeaf cond end out$/root/row/ship_to :) , 
  (: optLeaf out$/root/row/bill_to :) let $opt := (
(: value out$/root/row/bill_to :)
let $unconvertedValue := (
  (: missingCheck :) if (fn:count($out_Map_root_Map_row/Map:bill_to[1]) = 0) then ('_789odtMissing') else (
  (: nullCheck :) if ($out_Map_root_Map_row/Map:bill_to[1]/@xsi:nil['true']) then ('_osdt987Null') else ($out_Map_root_Map_row/Map:bill_to[1]/text()) (: missingCheck :)) (: nullCheck :) ) return if ( $unconvertedValue[1] cast as xs:string? = '_osdt987Null') then () else  text { $unconvertedValue } 
  )
 (: optLeaf cond out$/root/row/bill_to :) return if ($opt[last()] cast as xs:string? = '_789odtMissing') then () else if (fn:string-length(fn:string-join($opt[last()],'')) > 0) then 
(: element out$/root/row/bill_to :) element Map:bill_to { $opt
(: element end  out$/root/row/bill_to :) }
  else ()  (: optLeaf cond end out$/root/row/bill_to :) , 
  (: optLeaf out$/root/row/vendor :) let $opt := (
(: value out$/root/row/vendor :)
let $unconvertedValue := (
  (: missingCheck :) if (fn:count($out_Map_root_Map_row/Map:vendor_code[1]) = 0) then ('_789odtMissing') else (
  (: nullCheck :) if ($out_Map_root_Map_row/Map:vendor_code[1]/@xsi:nil['true']) then ('_osdt987Null') else ($out_Map_root_Map_row/Map:vendor_code[1]/text()) (: missingCheck :)) (: nullCheck :) ) return if ( $unconvertedValue[1] cast as xs:string? = '_osdt987Null') then () else  text { $unconvertedValue } 
  )
 (: optLeaf cond out$/root/row/vendor :) return if ($opt[last()] cast as xs:string? = '_789odtMissing') then () else if (fn:string-length(fn:string-join($opt[last()],'')) > 0) then 
(: element out$/root/row/vendor :) element Map:vendor { $opt
(: element end  out$/root/row/vendor :) }
  else ()  (: optLeaf cond end out$/root/row/vendor :) , 
  (: optLeaf out$/root/row/ord_date :) let $opt := (
(: value out$/root/row/ord_date :)
let $unconvertedValue := (
  (: missingCheck :) if (fn:count($out_Map_root_Map_row/Map:ord_date[1]) = 0) then ('_789odtMissing') else (
  (: nullCheck :) if ($out_Map_root_Map_row/Map:ord_date[1]/@xsi:nil['true']) then (()) else (com_oaklandsw_transform_runtime_RuntimeSupport:convertDataDate(true(),30,0,-1,-1,'/root/row/ord_date',$out_Map_root_Map_row/Map:ord_date[1]/text() cast as xs:string?)) (: missingCheck :)) (: nullCheck :) ) return if ( $unconvertedValue[1] cast as xs:string? = '_osdt987Null') then () else  text { com_oaklandsw_transform_runtime_RuntimeSupport:convertDataDate
  (false(),30,0,-1,-1,'/root/row/ord_date',$unconvertedValue cast as xs:string?)(:fc:) } 
  )
 (: optLeaf cond out$/root/row/ord_date :) return if ($opt[last()] cast as xs:string? = '_789odtMissing') then () else if (fn:string-length(fn:string-join($opt[last()],'')) > 0) then 
(: element out$/root/row/ord_date :) element Map:ord_date { $opt
(: element end  out$/root/row/ord_date :) }
  else ()  (: optLeaf cond end out$/root/row/ord_date :) , 
  (: optLeaf out$/root/row/ship_date :) let $opt := (
(: value out$/root/row/ship_date :)
let $unconvertedValue := (
  (: missingCheck :) if (fn:count($out_Map_root_Map_row/Map:ship_date[1]) = 0) then ('_789odtMissing') else (
  (: nullCheck :) if ($out_Map_root_Map_row/Map:ship_date[1]/@xsi:nil['true']) then (()) else (com_oaklandsw_transform_runtime_RuntimeSupport:convertDataDate(true(),30,0,-1,-1,'/root/row/ship_date',$out_Map_root_Map_row/Map:ship_date[1]/text() cast as xs:string?)) (: missingCheck :)) (: nullCheck :) ) return if ( $unconvertedValue[1] cast as xs:string? = '_osdt987Null') then () else  text { com_oaklandsw_transform_runtime_RuntimeSupport:convertDataDate
  (false(),30,0,-1,-1,'/root/row/ship_date',$unconvertedValue cast as xs:string?)(:fc:) } 
  )
 (: optLeaf cond out$/root/row/ship_date :) return if ($opt[last()] cast as xs:string? = '_789odtMissing') then () else if (fn:string-length(fn:string-join($opt[last()],'')) > 0) then 
(: element out$/root/row/ship_date :) element Map:ship_date { $opt
(: element end  out$/root/row/ship_date :) }
  else ()  (: optLeaf cond end out$/root/row/ship_date :) , 
  (: optLeaf out$/root/row/line_no :) let $opt := (
(: value out$/root/row/line_no :)
let $unconvertedValue := (
  (: missingCheck :) if (fn:count($out_Map_root_Map_row/Map:line_no[1]) = 0) then ('_789odtMissing') else (
  (: nullCheck :) if ($out_Map_root_Map_row/Map:line_no[1]/@xsi:nil['true']) then (0) else ($out_Map_root_Map_row/Map:line_no[1]/text()) (: missingCheck :)) (: nullCheck :) ) return if ( $unconvertedValue[1] cast as xs:string? = '_osdt987Null') then () else  text { $unconvertedValue } 
  )
 (: optLeaf cond out$/root/row/line_no :) return if ($opt[last()] cast as xs:string? = '_789odtMissing') then () else if (fn:string-length(fn:string-join($opt[last()],'')) > 0) then 
(: element out$/root/row/line_no :) element Map:line_no { $opt
(: element end  out$/root/row/line_no :) }
  else ()  (: optLeaf cond end out$/root/row/line_no :) , 
  (: optLeaf out$/root/row/item_code :) let $opt := (
(: value out$/root/row/item_code :)
let $unconvertedValue := (
  (: missingCheck :) if (fn:count($out_Map_root_Map_row/Map:item_code[1]) = 0) then ('_789odtMissing') else (
  (: nullCheck :) if ($out_Map_root_Map_row/Map:item_code[1]/@xsi:nil['true']) then ('_osdt987Null') else ($out_Map_root_Map_row/Map:item_code[1]/text()) (: missingCheck :)) (: nullCheck :) ) return if ( $unconvertedValue[1] cast as xs:string? = '_osdt987Null') then () else  text { $unconvertedValue } 
  )
 (: optLeaf cond out$/root/row/item_code :) return if ($opt[last()] cast as xs:string? = '_789odtMissing') then () else if (fn:string-length(fn:string-join($opt[last()],'')) > 0) then 
(: element out$/root/row/item_code :) element Map:item_code { $opt
(: element end  out$/root/row/item_code :) }
  else ()  (: optLeaf cond end out$/root/row/item_code :) , 
  (: optLeaf out$/root/row/ord_qty :) let $opt := (
(: value out$/root/row/ord_qty :)
let $unconvertedValue := (
  (: missingCheck :) if (fn:count($out_Map_root_Map_row/Map:ord_qty[1]) = 0) then ('_789odtMissing') else (
  (: nullCheck :) if ($out_Map_root_Map_row/Map:ord_qty[1]/@xsi:nil['true']) then (0) else ($out_Map_root_Map_row/Map:ord_qty[1]/text()) (: missingCheck :)) (: nullCheck :) ) return if ( $unconvertedValue[1] cast as xs:string? = '_osdt987Null') then () else  text { $unconvertedValue } 
  )
 (: optLeaf cond out$/root/row/ord_qty :) return if ($opt[last()] cast as xs:string? = '_789odtMissing') then () else if (fn:string-length(fn:string-join($opt[last()],'')) > 0) then 
(: element out$/root/row/ord_qty :) element Map:ord_qty { $opt
(: element end  out$/root/row/ord_qty :) }
  else ()  (: optLeaf cond end out$/root/row/ord_qty :) 
(: element end  out$/root/row :) }
(: seq end in$/root/row :) )
};


(: CG context elem  out: out$/root in: in$/root loop: $out_Map_root inScope?: true :)
declare function local:out_Map_root($top as node())
{
(: seq in$/root :)
  for $out_Map_root at $out_Map_root_i in let $inVar := $top/Map:root return  $inVar 
 return (: for :)  (
(: element out$/root :) element Map:root { (), local:out_Map_root_Map_row($top,$out_Map_root,$out_Map_root_i)

(: element end  out$/root :) }
(: seq end in$/root :) )
};

(: OUTPUT - Start :)
local:out_Map_root(/)
(: OUTPUT - End :)