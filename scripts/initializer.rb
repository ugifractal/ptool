begin
require 'java'
import 'javax.swing.JPanel'
import 'javax.swing.JLabel'
import 'javax.swing.JTextField'
import 'javax.swing.JPasswordField'
import 'javax.swing.JComboBox'
import 'javax.swing.JTabbedPane'
import 'javax.swing.JScrollPane'
import 'javax.swing.JButton'
import 'javax.swing.JTable'
import 'javax.swing.table.DefaultTableModel'
import 'javax.swing.BorderFactory'
import 'java.awt.Dimension'
import 'com.ugi.gui.ComboItem'
import 'eszrecord.model.Connector'
import 'ptools.Terminal'
import 'ptools.TerminalHandler'
require 'profligacy/swing'
require 'profligacy/lel'

#load 'scripts/models/voucher_buy.rb'
#load 'scripts/models/voucher_type.rb'
include Profligacy




  class DlgListener
    include com.ugi.gui.DialogListener
    def initialize(&block)
      super
      @blk = block
    end
    
    def perform
      @blk.call
    end
  end
  
  class TermHandler < TerminalHandler
    def initialize(&block)
      super
      @blk = block
    end
    
    def loaded(data)
      @blk.call(data)
    end
  end
  
  class Terminal
    def send_AT(cmd, timeout, &block)
      executeAT(cmd, timeout, TermHandler.new(&block))
    end
  end
  
  class Java::JavaxSwing::JPanel   
    def initialize
      super
      setBorder(BorderFactory.createEtchedBorder())
    end

    def set_dim(width, height)
      setPreferredSize(Dimension.new(width, height))
    end

    def out_flow_container
      panel = JPanel.new
      panel.add(self)
      panel
    end
    
    
    def out_dialog(&block)
      dlg = com.ugi.gui.Dialog.new($frame, false)
      dlg.set_layer_pane(self)
      dlg.show_modal(DlgListener.new{block.call})
    end

  end
  
  class GUICreator
    def self.login
      layout = "[<lbl_username|<txt_username][lbl_password|txt_password]"
      ui = Swing::LEL.new(JPanel, layout) do |c,i|
        c.lbl_username = JLabel.new "User name"
        c.txt_username = JTextField.new 15
        c.lbl_password = JLabel.new "Password"
        c.txt_password = JPasswordField.new 15
      end
      panel = ui.build
      panel.set_dim(300,100);
      panel
    end
    
    def self.panel_buy
      layout = "[<lbl_no|(10,10)txt_no][lbl_amount|cb_amount][_|>(10,10)btn_send]"
      ui = Swing::LEL.new(JPanel, layout) do |c,i|
        c.lbl_no = JLabel.new("No HP")
        c.txt_no = JTextField.new 15
        c.lbl_amount = JLabel.new("Nominal")
        c.cb_amount = JComboBox.new
        VoucherType.list_voucher.each do |x|
          c.cb_amount.addItem(ComboItem.new(x[0],x[1]))
        end
        c.btn_send = JButton.new("Kirim")
        i.btn_send = {:action => proc do
          VoucherBuy.create(ui.txt_no.text, ui.cb_amount.get_selected_item.get_key.to_i)
        end }
      end
      panel = ui.build
      panel.set_dim(300,120)
      panel
    end
    
    def self.buy_table
      layout = "[px][tbl]"
      ui = Swing::LEL.new(JPanel, layout) do |c,i|
        c.px = JPanel.new
        c.px.add(panel_buy)
        tbl = JTable.new
        c.tbl = JScrollPane.new(tbl)
        columnNames = ['No Hp', 'Voucher']
        rowData = []
        10.times{rowData << columnNames}
        mymodel = DefaultTableModel.new(VoucherBuy.list.to_java, columnNames.to_java);
        tbl.setModel(mymodel);
      end
      panel = ui.build
      panel
    end
    
    

    def self.main_tabs
      layout = "[<tab_main]"
      ui = Swing::LEL.new(JPanel, layout) do |c,i|
        c.tab_main = JTabbedPane.new
        #p_buy = panel_buy.out_flow_container
        #p_buy.add(JTable.new)
        #p_buy.add(panel_buy)
        #c.tab_main.addTab("Isi pulsa", buy_table)
        #c.tab_main.addTab("History", panel_history)
        #c.tab_main.addTab("Setting", panel_setting)
        test = org.jdesktop.swingx.JXMapKit.new
        puts "xxx"
        
        x = org.jdesktop.swingx.JXMapKit::DefaultProviders::OpenStreetMaps
        puts x
        puts "xxx"
        test.setDefaultProvider(x)
        #test.setDataProviderCreditShown(true);

        c.tab_main.addTab("Setting", test)
        
      end
      panel = ui.build
    end
  end
  
  
  
  #$term.send_AT("AT+cdv=085220300110\015", 3000){|x| puts x}
  #$term.send_AT("AT+chv\015", 3000){|x| puts x}
  #$frame.getMainPanel().remove_all
  drv = "org.h2.Driver"
  aurl = "jdbc:h2:tcp://localhost/~/lapan"
  #VoucherBuy.listeners << LX.new
  unless $stmt
  #  Connector.get_instance.add_connection("sa", "",drv,aurl,"pulsa")
  #  $stmt = Connector.get_instance.get_active_connection.create_statement
  end
  $frame.getMainPanel().remove_all
  
  #GUICreator.login.out_dialog{ puts "sugiarto"}
  #$frame.getMainPanel().set_layout(java.awt.FlowLayout.new)
  
  $frame.getMainPanel().add(GUICreator.main_tabs)
  $frame.set_visible(true)
  
  
  #puts Connector.get_instance.get_active_connection
  #stmt = Connector.get_instance.get_active_connection.create_statement
  #stmt.execute_update(IO.read('scripts/script.sql'))
  
  
  
  #VoucherType.list_voucher
rescue Exception => e
  puts e
end

