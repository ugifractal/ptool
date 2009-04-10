import 'eszrecord.model.CommonModel'
import 'eszrecord.model.Connector'

class VoucherBuy
  #@@listeners = []
  def self.listeners
    #@@listeners << l
  end
  
  def self.create(number, voucher_type)
    stmt = Connector.get_instance.get_active_connection.create_statement
    stmt.execute_update("insert into voucher_buys (number, voucher_type)
      values('#{number}', #{voucher_type})
    ")
    #@@listeners.each do |l|
    #  l.after_create
    #end
  end
  
  def self.list
    rs = $stmt.execute_query("select * from voucher_buys order by id DESC")
    ret = []
    while rs.next
      number = rs.get_string('number')
      type = rs.get_int('voucher_type')
      ret << [number, type]
    end 
    return ret
  end
  
end