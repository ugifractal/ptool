class VoucherType
  def self.list_voucher
    stmt = Connector.get_instance.get_active_connection.create_statement
    rs = stmt.execute_query("select * from voucher_types")
    ret = []
    while rs.next
      name = rs.get_string('name')
      id = rs.get_int('id')
      ret << [id, name]
    end 
    return ret
  end
end