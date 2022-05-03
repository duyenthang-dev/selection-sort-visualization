package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.event.ActionEvent;

import javax.swing.UIManager.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class App implements ActionListener {
	// tạo mới các luồng
	private Thread[] threads = new Thread[1000000];
	// thread con hiện tại, -1 ứng với chưa tạo thread con
	private int cur = -1;
	private static final Color BLUE_DARKER = new Color(29,164,228);
	private static final Color BLUE_LIGHTER = new Color(201, 225, 245);
	private static final Color YELOW = new Color(251, 211, 166);
	private static final Color GREEN = new Color(76, 209, 55);
	private static final int WIDTH = 570;
    private static final int HEIGHT = 440;
	public JFrame myFrame;
	private final ButtonGroup selectInput = new ButtonGroup();
	public JPanel array[];
	public JTable table;
	
	public JLabel arrLabel[];	
	private JLabel appTitle;
	private JPanel tableArea;
	private JLabel labelChart;
	private JLabel tableTitle;
	private JLabel speedValue;
	
	private JScrollPane scrollPane;
	// tạo các JPanel component mainArea, arrayArea, buttonArea  để phân chia giao diện thành các phần riêng biệt
	private JPanel mainArea;
	public JPanel arrayArea;
	private JSlider slider;
	private JPanel buttonArea;
	private JPanel panel;
	private JPanel panel_2;
	
	private JButton btnStop;
	private JButton btnEnd;
	private JButton btnStart;
	private JButton btnAccept;
	private JRadioButton btnInput;
	private JRadioButton btnRandom;
	
	// mảng chứa các phần tử int cần sắp xếp
	private int arr[] = null;
	public Thread sortThread;
	// kiểm tra chương trình kết thúc hay chưa
	private boolean isEnd = false;
	// thời gian đợi của sort threads
	private int interval = 10000;
	// kiểm tra chương trình kết còn chạy kh
	private boolean isRunning = false;
	//tốc độ các lần tìm phần tử min
	private int speed = 0;
	// kiểu sắp xếp
	private boolean increasing = true;

	// hành động
	private enum Actions{
		INIT,
		RUN,
		STOP,
		CANCEL,
		INPUT,
		RANDOM
	}

	public void reDrawArray(int arr[], JPanel arrayWrapper, int idx1, int idx2) {
		arrayWrapper.removeAll();
		labelChart = new JLabel("Mô phỏng thuật toán");
		labelChart.setBounds(0, 0, 593, 40);
		labelChart.setHorizontalAlignment(SwingConstants.CENTER);
		labelChart.setFont(new Font("Tahoma", Font.BOLD, 18));
		arrayWrapper.add(labelChart);
		int arrLen = arr.length;
		int gap;
		if(arrLen < 7)
			gap = 40;
		else if(arrLen < 10)
			gap = 25;
		else if(arrLen < 13)
			gap = 15;
		else gap = 10;
		
        int min = 9999;
        int max = -9999;
        for(int i =0; i < arrLen; i++)
        {
            if(arr[i] < min)
                min = arr[i];
            if(arr[i] > max)
                max = arr[i];
        }
        
        // unitlength: tính độ dài của 1 đơn vị để vẽ các cột cho đúng tỉ lệ
    
        int base;
        if(min < 0)
        	base = min -2;
        else base = 0;
      
        int unitLength = (HEIGHT - 60) / Math.abs(max - base);
        System.out.println(unitLength);
        array = new JPanel[arrLen];
        arrLabel = new JLabel[arrLen];
        
        // vẽ hình
        for(int i =0; i < arrLen; i++){
            array[i]= new JPanel();
            if(i <= idx1)
            	array[i].setBackground(GREEN);
            else 
            	array[i].setBackground(BLUE_DARKER);
            int width = (WIDTH - 30)/arrLen - gap;
            int height = Math.abs((int)arr[i] - base) * unitLength;
            array[i].setBounds(30 + i*(width + gap), HEIGHT - height , width, height);
            // thêm chỉ số trên đầu các cột
            arrLabel[i] = new JLabel(Integer.toString(arr[i]));
            arrLabel[i].setFont(new Font("Tahoma", Font.PLAIN, 15));
            arrLabel[i].setHorizontalAlignment(JLabel.CENTER);
            
            arrLabel[i].setBounds(30 + i*(width + gap), HEIGHT - height -25 , width, 25);
            arrayWrapper.add(array[i]);
            arrayWrapper.add(arrLabel[i]);
        }
      
		arrayWrapper.repaint();
	}
	// vẽ biểu đồ
	public void drawArray(int arr[], JPanel arrayWrapper){
		//gap: khoảng cách giữa các cột
		int arrLen = arr.length;
		int gap;
		if(arrLen < 7)
			gap = 40;
		else if(arrLen < 10)
			gap = 25;
		else if(arrLen < 13)
			gap = 15;
		else gap = 10;
		
        int min = 9999;
        int max = -9999;
        for(int i =0; i < arr.length; i++)
        {
            if(arr[i] < min)
                min = arr[i];
            if(arr[i] > max)
                max = arr[i];
        }
        System.out.println("okeee");
        int base;
        if(min < 0)
        	base = min -2;
        else base = 0;
        // unitlength: tính độ dài của 1 đơn vị để vẽ các cột cho đúng tỉ lệ
        int unitLength = (HEIGHT - 60) / Math.abs(max - base);
       
        array = new JPanel[arr.length];
        arrLabel = new JLabel[arr.length];
        
        // vẽ hình
        for(int i =0; i < array.length; i++){
            array[i]= new JPanel();
            array[i].setBackground(BLUE_DARKER);
            int width = (WIDTH - 30)/array.length - gap;
            int height = Math.abs((int)arr[i] - base) * unitLength;
            array[i].setBounds(30 + i*(width + gap), HEIGHT - height , width, height);
            // thêm chỉ số trên đầu các cột
            arrLabel[i] = new JLabel(Integer.toString(arr[i]));
            arrLabel[i].setFont(new Font("Tahoma", Font.PLAIN, 15));
            arrLabel[i].setHorizontalAlignment(JLabel.CENTER);
            
            arrLabel[i].setBounds(30 + i*(width + gap), HEIGHT - height -25 , width, 25);
            arrayWrapper.add(array[i]);
            arrayWrapper.add(arrLabel[i]);
        }
      
		arrayWrapper.repaint();
    }
	
	//thêm hàng dữ liệu vào bảng
	public void drawTable(int arr[], JScrollPane scrollPane ){
        
        DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);
       
        table.setPreferredScrollableViewportSize(new Dimension(573, 390));
        String[] colNames = new String[arr.length + 1];
        //tạo các header
        Object a[] = new Object[arr.length +1];       
        a[0] = "Ban đầu"; // ban đầu chưa sắp xếp nên để tên là "ban đầu"
        colNames[0] = "Ban đầu";
        //thêm cột đầu tiên vào bảng
        model.addColumn(colNames[0]);
       
        for(int i =0; i < arr.length; i++)
        {
            a[i + 1] = (Object) Integer.toString(arr[i]);
            colNames[i + 1] = Integer.toString(arr[i]);
            model.addColumn(colNames[i+ 1]);
            table.getColumnModel().getColumn(i + 1).setPreferredWidth(75);
        }      
        table.getColumnModel().getColumn(0).setPreferredWidth(150); 
        table.setRowHeight(30);
        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        this.scrollPane.setViewportView(table);
      
    }
	
	// cập nhật lại bảng sau mỗi lần chạy 
	public void reDrawTable(int arr[], JTable table, int turn) {
		System.out.println("chay ham redraw table");
		// lấy model để thêm dữ liệu
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		Object row[] = new Object[arr.length + 1];
		row[0] = "Lần " + turn;
		for(int i =0; i < arr.length; i++)
		{
			row[i + 1] = Integer.toString(arr[i]);
		}
		model.addRow(row);
	
	}
	public void clear()
	{
		this.arrayArea.removeAll();
		labelChart = new JLabel("Mô phỏng thuật toán");
		labelChart.setBounds(0, 0, 593, 40);
		labelChart.setHorizontalAlignment(SwingConstants.CENTER);
		labelChart.setFont(new Font("Tahoma", Font.BOLD, 18));
		this.arrayArea.add(labelChart);
		this.arrayArea.repaint();
		
		this.tableArea.removeAll();
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 50, 573, 390);
		scrollPane.setBackground(SystemColor.menu);
		tableArea.add(scrollPane);
		
		tableTitle = new JLabel("Kết quả sau mỗi lần chạy");
		tableTitle.setBackground(SystemColor.menu);
		tableTitle.setHorizontalAlignment(SwingConstants.CENTER);
		tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		tableTitle.setBounds(0, 0, 593, 40);
		tableArea.add(tableTitle);
		this.tableArea.repaint();
	}
	public void run(int arr[]) throws InterruptedException
	{
		// set khoảng thời gian thread chính đợi các threads con ( sort, swap, moving)
		interval = speed * (arr.length) + 4000; 
		sortThread = new Thread(sortProcess);
		// kích hoạt threads
    	sortThread.start();  	
	}
	public void init()
	{
		clear();
		drawTable(arr, scrollPane);
		drawArray(arr, this.arrayArea);
	}
	public void stop ()
	{
		int b = 0;
		while(true)
		{
			//lấy từng threads t trong dãy threads đang có
			for (Thread t : Thread.getAllStackTraces().keySet()) 
			{  
				try {
					// dừng lần lượt từng threads này
					t.interrupt();
				}
				catch (Exception e) {}
			} 
			b++;
		
			if(b == arr.length)
				break;
		}
		cur = -1;
	}
	public void Swap(int i, int minIndex, App frame, int arr[])
    {
    	
    	// p1: i, p2: minIndex
		// tăng số threads lên 1 đơn vị
    	cur++;
    	threads[cur] = new Thread(new Runnable() {
			int min = i;
			@Override
			public void run() {
				try {
					// các threads con sẽ đợi lẫn nhau
					if(cur != 0)
						threads[cur - 1].join();

					frame.array[i].setBackground(YELOW); // processing color
					for(int j = i + 1; j < arr.length; j++)
					{
						boolean compare = false;
						if(increasing)
							compare = (arr[j] < arr[min])? true: false;
						else 
							compare = (arr[j] > arr[min])? true: false;
						
						frame.array[j].setBackground(BLUE_LIGHTER); // xanh nhat
						Thread.sleep(speed);
						// nếu chọn được phần tử như mong muốn
						if(compare)
						{
							frame.array[j].setBackground(YELOW); // vang
							min = j;
							for(int k = i + 1; k < min; k++)
								frame.array[k].setBackground(BLUE_DARKER);		
						}						
						else 
							frame.array[j].setBackground(BLUE_DARKER); // xanh ddajm
					}
							
					Thread.sleep(speed);
					/*
					 * Mô phỏng quá trình hoán đổi 2 cột
					 * */
					// tìm vị trí x của 2 cột cần hoán đổi				
					int x1 = frame.array[i].getX();
					int x2 = frame.array[minIndex].getX();
					//tìm khoảng cách giữa 2 cột cần hoán đổi
					int distance = Math.abs(x1 - x2);
					//* tính khoảng cách giữa mỗi bước dịch chuyển, tối thiểu là 2
				
					int step = distance/50;
					if(step < 2)
						step = 2;
					
					// thực hiện vẽ chuyển động swap
					while(distance > 0)
					{			
//						System.out.println(distance);
						// tính khoảng cách còn lại giữa 2 cột sau mỗi lần dịch chuyển
						if(distance > step)
							distance = distance - step;
						else {	
							step = distance;
							distance = 0; 
						}
						
						frame.arrLabel[i].setLocation(frame.array[i].getX() + step, frame.array[i].getY());
						frame.array[i].setLocation(frame.array[i].getX() + step, frame.array[i].getY());
						
						frame.arrLabel[minIndex].setLocation(frame.array[minIndex].getX() - step, frame.array[minIndex].getY());
						frame.array[minIndex].setLocation(frame.array[minIndex].getX() - step, frame.array[minIndex].getY());						
						Thread.sleep(40);
					}
					// set vị trí cho 2 cột sau khi đã chuyển động
					frame.array[minIndex].setBackground(new Color(76, 209, 55)); // xanh lá cây
					frame.array[i].setLocation(x2, frame.array[i].getY());
					frame.arrLabel[i].setLocation(x2, frame.arrLabel[i].getY());	
					frame.array[minIndex].setLocation(x1, frame.array[minIndex].getY());
					frame.arrLabel[minIndex].setLocation(x1, frame.arrLabel[minIndex].getY());				
					
					// đổi vị trí phần tử của mảng arr 
					// arr = [2, 4, 2, 6, 7]
					int temp = arr[i];
				    arr[i] = arr[minIndex];
				    arr[minIndex] = temp;
				   //[1, 4, 5, 6]
				    if(!isEnd)
				    	frame.reDrawArray(arr,frame.arrayArea, i, minIndex);			  
				}
				catch(Exception e) {}		
			}
		});	
    	threads[cur].start();    	
    }
	
	App a = this;
	Runnable sortProcess = new Runnable() {		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("chay ham run() cua sortProcess");
			// chạy thuật toán
			for(int i =0; i < arr.length; i++)
			{
					
				int minIndex = i;
				for(int j = i + 1; j < arr.length; j++)
				{
					if(increasing)
					{
						if (arr[j] < arr[minIndex]) {
			                minIndex = j;
			           }
					}
					else
					{
						if (arr[j] > arr[minIndex]) {
			                minIndex = j;
			           }
					}
					
				}
				System.out.println("Lan " + i + "min = " + arr[minIndex]);
				if(i != 0)
					reDrawTable(arr, table, i);
				
				Swap(i, minIndex, a, arr);
				
				System.out.println(interval);
					try {
						// đợi các swap thread - threads[cur] chạy xong
						Thread.sleep(interval);
					} catch (InterruptedException e) {
					}
				interval -= speed;
			}
			isRunning = false;
			JOptionPane.showMessageDialog(null, "Đã sắp xếp xong!", "Successfull", JOptionPane.INFORMATION_MESSAGE);
			return;		
		}
	};
	private JPanel panel_1;
	private JTextField inputField;
		
    	
	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		myFrame = new JFrame();
		myFrame.setBackground(SystemColor.menu);
		myFrame.getContentPane().setBackground(new Color(199, 54, 95));
		myFrame.setTitle("Sắp xếp chọn");
		myFrame.setBounds(0, 0, 1200, 670);
		myFrame.setLocationRelativeTo(null);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.getContentPane().setLayout(null);
		
		appTitle = new JLabel("THUẬT TOÁN SẮP XẾP CHỌN");
		appTitle.setBackground(new Color(199, 54, 95));
		appTitle.setForeground(Color.WHITE);
		appTitle.setBounds(0, 0, 1186, 40);
		appTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
		appTitle.setHorizontalAlignment(SwingConstants.CENTER);
		myFrame.getContentPane().add(appTitle);
		
		mainArea = new JPanel();
		mainArea.setBackground(SystemColor.menu);
		mainArea.setBounds(new Rectangle(0, 40, 1186, 450));
		myFrame.getContentPane().add(mainArea);
		mainArea.setLayout(new GridLayout(0, 2, 0, 0));
		
		tableArea = new JPanel();
		tableArea.setBackground(SystemColor.menu);
		tableArea.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		mainArea.add(tableArea);
		tableArea.setLayout(null);
		
		tableTitle = new JLabel("Kết quả sau mỗi lần chạy");
		tableTitle.setBackground(SystemColor.menu);
		tableTitle.setHorizontalAlignment(SwingConstants.CENTER);
		tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		tableTitle.setBounds(0, 0, 593, 40);
		tableArea.add(tableTitle);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 50, 573, 390);
		scrollPane.setBackground(SystemColor.menu);
		tableArea.add(scrollPane);
		
		arrayArea = new JPanel();
		arrayArea.setBackground(SystemColor.menu);
		arrayArea.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		mainArea.add(arrayArea);
		arrayArea.setLayout(null);
		
		labelChart = new JLabel("Mô phỏng thuật toán");
		labelChart.setBounds(0, 0, 593, 40);
		labelChart.setHorizontalAlignment(SwingConstants.CENTER);
		labelChart.setFont(new Font("Tahoma", Font.BOLD, 18));
		arrayArea.add(labelChart);
		
		buttonArea = new JPanel();
		buttonArea.setBackground(SystemColor.menu);
		buttonArea.setBounds(0, 489, 1186, 150);
		myFrame.getContentPane().add(buttonArea);
		buttonArea.setLayout(null);
		
		panel = new JPanel();
		panel.setBackground(SystemColor.menu);
		panel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		panel.setBorder(new TitledBorder(null, "Chọn tốc độ  ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(456, 10, 309, 120);
		buttonArea.add(panel);
		panel.setLayout(null);
		
		slider = new JSlider();
		slider.setPaintLabels(true);
		
		slider.setPaintTicks(true);
		slider.setMinimum(100);
		slider.setMaximum(900);
		slider.setMinorTickSpacing(400);
		slider.setValue(500);
		slider.setBounds(41, 50, 153, 22);
		panel.add(slider);
		
		speedValue = new JLabel("500ms");
		speedValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
		speedValue.setBounds(216, 50, 83, 20);
		panel.add(speedValue);
		
		panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.menu);
		panel_1.setBorder(new TitledBorder(null, "Chọn kiểu nhập mảng  ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panel_1.setBounds(30, 10, 396, 120);
		buttonArea.add(panel_1);
		panel_1.setLayout(null);
		
		btnRandom = new JRadioButton("Random mảng");
		btnRandom.setBounds(39, 25, 125, 21);
		panel_1.add(btnRandom);
		selectInput.add(btnRandom);
		btnRandom.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		btnInput = new JRadioButton("Nhập mảng");
		
		btnInput.setBounds(39, 48, 112, 21);
		panel_1.add(btnInput);
		selectInput.add(btnInput);
		btnInput.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		btnAccept = new JButton("Xác nhận");
		
		btnAccept.setBounds(39, 75, 100, 30);
		panel_1.add(btnAccept);
		btnAccept.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		inputField = new JTextField();
		inputField.setBounds(157, 45, 208, 30);
		inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panel_1.add(inputField);
		inputField.setColumns(10);
		inputField.setText("");
		// ban đầu ẩn input fields
		inputField.setVisible(false);
		
		panel_2 = new JPanel();
		panel_2.setBackground(SystemColor.menu);
		panel_2.setBorder(new TitledBorder(null, "Hành Động  ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(800, 10, 350, 120);
		buttonArea.add(panel_2);
		panel_2.setLayout(null);
		
		btnStop = new JButton("Kết thúc");
		btnStop.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnStop.setBounds(50, 30, 100, 30);
		panel_2.add(btnStop);
		
		btnEnd = new JButton("Thoát");
		btnEnd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnEnd.setBounds(50, 75, 100, 30);
		panel_2.add(btnEnd);
		
		btnStart = new JButton("Bắt đầu");
		btnStart.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnStart.setBounds(185, 30, 100, 30);
		panel_2.add(btnStart);
		
		table = new JTable();
		
	}
	
	/**
	 * Launch the application.
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
					App window = new App();
					window.myFrame.setVisible(true);
					window.btnAccept.setActionCommand(Actions.INIT.name());
					window.btnAccept.addActionListener(window);
					
					window.btnStart.setActionCommand(Actions.RUN.name());
					window.btnStart.addActionListener(window);
					
					window.btnStop.setActionCommand(Actions.STOP.name());
					window.btnStop.addActionListener(window);
					
					window.btnEnd.setActionCommand(Actions.CANCEL.name());
					window.btnEnd.addActionListener(window);
					
					window.btnInput.addItemListener(new ItemListener() {
						public void itemStateChanged(ItemEvent e) {
							if(e.getStateChange() == ItemEvent.SELECTED)
							{
								
								window.inputField.setVisible(true);
							}
							else if(e.getStateChange() == ItemEvent.DESELECTED)
							{
								window.inputField.setText("");
								window.inputField.setVisible(false);
							}
						}
					});
					
					window.slider.addChangeListener(new ChangeListener() {
						
						@Override
						public void stateChanged(ChangeEvent e) {
							// TODO Auto-generated method stub
							window.speedValue.setText(window.slider.getValue() + " ms");
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	@Override
	// callback function
	public void actionPerformed(ActionEvent e){
		// TODO Auto-generated method stub
		if(e.getActionCommand() == Actions.STOP.name())
		{
			System.out.println("Ket thuc");
			this.isRunning = false;
			stop();
			clear();	
		}
		else if(e.getActionCommand() == Actions.INIT.name())
		{
			// không chọn option nào
			if(isRunning)
				return;
			
			if(!this.btnInput.isSelected() && !this.btnRandom.isSelected())
			{
				JOptionPane.showMessageDialog(null, "Bạn chưa chọn kiểu nhập mảng!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			// chọn option nhập mảng
			else if(this.btnInput.isSelected())
			{
				String arrInput = this.inputField.getText();
				// "1 2 3 4 5"
				String[] strs = arrInput.split("\\s+");// space
				// strs = ["1" , "2", "3" , "4" , "5"]
				// nếu mảng input rỗng
				if(arrInput.isEmpty())
				{		
					JOptionPane.showMessageDialog(null, "Mảng input rỗng! Vui lòng nhập thêm giá trị", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				// néuw người dùng nhập nhiều hơn 13 phần tử thì chỉ lấy 13 số đầu
				int size = strs.length;
				if(size > 13)
					size = 13;
				arr = new int[size];
				for(int i =0; i < arr.length; i++)
				{
					//chuyển "1" -> thành số 1
					arr[i] = Integer.valueOf(strs[i]);
				}
				//arr = [1,2,3,4,5];
			}
			// chọn option random
			else if(this.btnRandom.isSelected())
			{
				Random rm = new Random();
				int size = 11;
				arr = new int[size];
				for(int i =0; i < size; i++)
				{
					//random 1 - 50
					int num = 1 + rm.nextInt(50 - 1 + 1);
					arr[i] = num;
				}
			}
			init();
			this.myFrame.repaint();
			
		}
		else if(e.getActionCommand() == Actions.RUN.name())
		{
			try {
				if(isRunning)
					return;				
				if(arr == null)
				{
					JOptionPane.showMessageDialog(null, "Bạn chưa nhập mảng", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				this.isRunning = true;
				// đặt tốc độ bằng giá trị trong slider
				this.speed = this.slider.getValue();
				run(this.arr);
				
				
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(e.getActionCommand() == Actions.CANCEL.name())
		{
			System.out.println("End ");
			System.exit(0);
		}	
	}		
}

