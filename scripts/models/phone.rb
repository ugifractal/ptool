class Phone
  def self.call(number)
    $term.send_AT("AT+CDV=#{number}\015",3000){|x| puts x}
  end
end