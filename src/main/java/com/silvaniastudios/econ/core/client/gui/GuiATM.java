package com.silvaniastudios.econ.core.client.gui;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.core.EconConfig;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.core.blocks.ATMContainer;
import com.silvaniastudios.econ.core.blocks.ATMEntity;
import com.silvaniastudios.econ.core.items.DebitCardItem;
import com.silvaniastudios.econ.network.ATMWithdrawPacket;
import com.silvaniastudios.econ.network.SoundPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiATM extends GuiContainer {
	
	private static final ResourceLocation TEXTURE_LEFT = new ResourceLocation(FurenikusEconomy.MODID, "textures/gui/atm_left.png");
    private static final ResourceLocation TEXTURE_RIGHT = new ResourceLocation(FurenikusEconomy.MODID, "textures/gui/atm_right.png");
    
    ATMEntity te;

    public GuiATM(ATMEntity entity, ATMContainer inventorySlotsIn) {
		super(inventorySlotsIn);
		this.te = entity;
	}
    
    public EconUtils econ = new EconUtils();

    protected int xSize = 362;
    protected int ySize = 256;
    
    String enteredPin = "";
    int pinAttempt = 1;
    int guiStage = 1;
    String withdrawCustom = "";
    long withdrawAmount;
    long initBalance;
    
    private int tick = 0;
    
    //GUI Stage changes the current active screen. It enables different features on the buttons, and changes the text.
    //Stage 1 asks for pin and waits for it to be entered.
    //Stage 2 is an options menu, asking for your next move.
    //Stage 3 is the Withdraw screen, the buttons will give money and remove from your NBT
    //Stage 4 is the Balance screen, and shows your balance.
    //Stage 5 is the withdraw confirm screen.
    //Stage 6 is the insufficient funds error message.
    //Stage 7 is the Withdraw X Amount screen.
    //Stage 8 is for no valid card found.
    //Stage 9+ informs of an invalid selection
    
    @Override
    public void initGui() {
    	super.initGui();
    	buttonList.clear();
    	int x = (width/2) - (xSize/2);
    	int y = (height/2) - (ySize/2);
    	FurenikusEconomy.network.sendToServer(new SoundPacket(FurenikusEconomy.MODID + ":cardInsert"));
    	buttonList.add(new ATMButton(1,  x + 50,  y + 215, 20, 15, "7", 0)); // 7
    	buttonList.add(new ATMButton(2,  x + 74,  y + 215, 20, 15, "8", 0)); // 8
    	buttonList.add(new ATMButton(3,  x + 98,  y + 215, 20, 15, "9", 0)); // 9
    	buttonList.add(new ATMButton(4,  x + 126, y + 177, 20, 15, "", 40)); // Cancel
    	buttonList.add(new ATMButton(5,  x + 50,  y + 196, 20, 15, "4", 0)); // 4
    	buttonList.add(new ATMButton(6,  x + 74,  y + 196, 20, 15, "5", 0)); // 5
    	buttonList.add(new ATMButton(7,  x + 98,  y + 196, 20, 15, "6", 0)); // 6
    	buttonList.add(new ATMButton(8,  x + 126, y + 196, 20, 15, "", 60)); // Clear
    	buttonList.add(new ATMButton(9,  x + 50,  y + 177, 20, 15, "1", 0)); // 1
    	buttonList.add(new ATMButton(10, x + 74,  y + 177, 20, 15, "2", 0)); // 2
    	buttonList.add(new ATMButton(11, x + 98,  y + 177, 20, 15, "3", 0)); // 3
    	buttonList.add(new ATMButton(12, x + 126, y + 215, 20, 15, "", 20)); // Confirm
    	buttonList.add(new ATMButton(13, x + 74,  y + 234, 20, 15, "0", 0)); // 0
    	
    	buttonList.add(new ATMButton(14, x + 17, y +  27, 20, 15, "", 0)); //Top-Left
    	buttonList.add(new ATMButton(16, x + 17, y +  58, 20, 15, "", 0)); //Mid-Upper Left
    	buttonList.add(new ATMButton(18, x + 17, y +  89, 20, 15, "", 0)); //Mid-Lower Left
    	buttonList.add(new ATMButton(20, x + 17, y + 120, 20, 15, "", 0)); //Bottom Left
    	
    	buttonList.add(new ATMButton(15, x + 251, y +  27, 20, 15, "", 0)); //Top-Right
    	buttonList.add(new ATMButton(17, x + 251, y +  58, 20, 15, "", 0)); //Mid-Upper Right
    	buttonList.add(new ATMButton(19, x + 251, y +  89, 20, 15, "", 0)); //Mid-Lower Right
    	buttonList.add(new ATMButton(21, x + 251, y + 120, 20, 15, "", 0)); //Bottom Right
    	
    	if (!econ.hasOwnCard(Minecraft.getMinecraft().player)) {
    		guiStage = 8;
    	}
    }
    
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        
        this.mc.getTextureManager().bindTexture(TEXTURE_LEFT);
		drawTexturedModalRect(left, top, 0, 0, 256, ySize);
		
		this.mc.getTextureManager().bindTexture(TEXTURE_RIGHT);
		drawTexturedModalRect(left+256, top, 0, 0, 128, ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		int x = -93;
    	int y = -45;
        int centre = x + 144;
        int leftButtonText = x + 48;
        int rightButtonText = x + 240;
        
        int buttonTop = y + 31;
        int buttonTopMid = y + 62;
        int buttonBottomMid = y + 93;
        int buttonBottom = y + 124;
        
        int fontColour = 0x007F0E;
    	
    	fontRenderer.drawString("ATM stage " + guiStage, x, y, 0x404040);
    	String bal = econ.formatBalance(initBalance);
    	String shortAmt = econ.formatBalance(withdrawAmount - initBalance);
    	String underScore = "";
    	if (tick < 80) {
    		tick++;
    	} else if (tick >= 80) {
    		tick = 0;
    	}
    	
    	if (tick < 40) {
    		underScore = "_";
    	}
    	
    	if (guiStage == 1) {
    		drawCenteredString(fontRenderer, "Welcome!", centre, y + 32, fontColour);
    		drawCenteredString(fontRenderer, "Please enter your PIN,", centre, y + 42, fontColour);
    		drawCenteredString(fontRenderer, "followed by 'Confirm'", centre, y + 52, fontColour);
	    	String maskedPin = "****";
    		if (enteredPin.length() == 0) {
    			maskedPin = underScore;
    		} else if (enteredPin.length() == 1) {
    			maskedPin = "*" + underScore;
    		} else if (enteredPin.length() == 2) {
    			maskedPin = "**" + underScore;
    		} else if (enteredPin.length() == 3) {
    			maskedPin = "***" + underScore;
    		}
    		fontRenderer.drawString(maskedPin, centre - fontRenderer.getStringWidth("**"), y + 66, fontColour);
	    	if (pinAttempt == 4) {
	    		drawCenteredString(fontRenderer, "Attempt 3 of 3.", centre, y + 80, fontColour);
	    		drawCenteredString(fontRenderer, "Card declined!", centre, y + 90, fontColour);
	    	} else {
	    		drawCenteredString(fontRenderer, "Attempt " + pinAttempt + " of 3.", centre, y + 80, fontColour);
	    	}
    	}
    	if (guiStage == 2) {
        	fontRenderer.drawString("Withdraw", leftButtonText, buttonTop, fontColour);
        	drawRightString(fontRenderer, "Balance", rightButtonText, buttonTop, fontColour);
        	drawRightString(fontRenderer, "Eject Card", rightButtonText, buttonBottom, fontColour);
    	}
    	if (guiStage == 3) {
    		drawCenteredString(fontRenderer, "Please select the amount", centre, y + 32, fontColour);
    		drawCenteredString(fontRenderer, "you wish to withdraw.", centre, y + 42, fontColour);
        	fontRenderer.drawString("10", leftButtonText, buttonTopMid, fontColour);
        	fontRenderer.drawString("50", leftButtonText, buttonBottomMid, fontColour);
        	fontRenderer.drawString("250", leftButtonText, buttonBottom, fontColour);
        	drawRightString(fontRenderer, "20", rightButtonText, buttonTopMid, fontColour);
        	drawRightString(fontRenderer, "100", rightButtonText, buttonBottomMid, fontColour);
        	drawRightString(fontRenderer, "Input Amount", rightButtonText, buttonBottom, fontColour);
    	}
    	if (guiStage == 4) {
    		drawCenteredString(fontRenderer, "Your current balance is: ", centre, y + 32, fontColour);
    		drawCenteredString(fontRenderer, bal, centre, y + 42, fontColour);
    		fontRenderer.drawString("Back", leftButtonText, buttonBottom, fontColour);
    	}
    	if (guiStage == 5) {
    		drawCenteredString(fontRenderer, "Withdrawl Successful!", centre, y + 32, fontColour);
    		drawCenteredString(fontRenderer, "You have withdrawn", centre, y + 42, fontColour);
    		drawCenteredString(fontRenderer, econ.formatBalance(withdrawAmount*100), centre, y + 52, fontColour);
    		drawCenteredString(fontRenderer, "Press Confirm to continue.", centre, y + 72, fontColour);

    	}
    	if (guiStage == 6) {
    		drawCenteredString(fontRenderer, "Insufficient Funds!", centre, y + 32, fontColour);
    		drawCenteredString(fontRenderer, shortAmt + " more needed!", centre, y + 42, fontColour);
    		drawRightString(fontRenderer, "Withdraw Less", rightButtonText, buttonTopMid, fontColour);
    		drawRightString(fontRenderer, "Return to Menu", rightButtonText, buttonBottomMid, fontColour);
    		drawRightString(fontRenderer, "Eject Card", rightButtonText, buttonBottom, fontColour);
    	}
    	if (guiStage == 7) {
    		drawCenteredString(fontRenderer, "Please enter the amount", centre, y + 32, fontColour);
    		drawCenteredString(fontRenderer, "you wish to withdraw:", centre, y + 42, fontColour);
    		drawCenteredString(fontRenderer, EconConfig.currencySign + withdrawCustom, centre, 24, fontColour);
    		drawRightString(fontRenderer, "Back", leftButtonText, buttonBottom, fontColour);
    	}
    	if (guiStage == 8) {
    		drawCenteredString(fontRenderer, "Welcome!", centre, y + 32, fontColour);
    		drawCenteredString(fontRenderer, "Please insert your card.", centre, y + 42, fontColour);
    	}
    	if (guiStage >= 9) {
    		drawCenteredString(fontRenderer, "Invalid Selection", centre, y + 32, fontColour);
    		drawRightString(fontRenderer, "Main Menu", rightButtonText, buttonBottom, fontColour);
    	}
	}
    
    public void actionPerformed(GuiButton guibutton) {
    	initBalance = econ.getBalance(Minecraft.getMinecraft().player);
    	FurenikusEconomy.log(2, "Balance available in GUI! Balance: $" + initBalance);
    	//Plays the sound, and while it's at it also updates client balance.
    	//Updating the balance on every button press means withdrawls etc will update the balance, and makes it effectively bulletproof
    	//against latency issues.
    	FurenikusEconomy.network.sendToServer(new SoundPacket(FurenikusEconomy.MODID + ":atmButton"));
    	if (guiStage == 1) {
    		switch(guibutton.id) {
	    	case 1:
	    		enteredPin = enteredPin + "7";
	    		this.isPinLongEnough(enteredPin);
	    		break;
			case 2:
				enteredPin = enteredPin + "8";
	    		this.isPinLongEnough(enteredPin);
				break;
	    	case 3:
	    		enteredPin = enteredPin + "9";
	    		this.isPinLongEnough(enteredPin);
	    		break;
	    	case 4:
	    		break;
	    	case 5:
	    		enteredPin = enteredPin + "4";
	    		this.isPinLongEnough(enteredPin);
	    		break;
	    	case 6:
	    		enteredPin = enteredPin + "5";
	    		this.isPinLongEnough(enteredPin);
	    		break;
	    	case 7:
	    		enteredPin = enteredPin + "6";
	    		this.isPinLongEnough(enteredPin);
	    		break;
	    	case 8:
	    		enteredPin = "";
	    		break;
	    	case 9:
	    		enteredPin = enteredPin + "1";
	    		this.isPinLongEnough(enteredPin);
	    		break;
	    	case 10:
	    		enteredPin = enteredPin + "2";
	    		this.isPinLongEnough(enteredPin);
	    		break;
	    	case 11:
	    		enteredPin = enteredPin + "3";
	    		this.isPinLongEnough(enteredPin);
	    		break;
	    	case 12:
	    		enteredPin = enteredPin + "c";
	    		this.isPinLongEnough(enteredPin);
	    		break;
	    	case 13:
	    		enteredPin = enteredPin + "0";
	    		this.isPinLongEnough(enteredPin);
	    		break;
	    	}
    	}
    	
    	if (guiStage == 2) {

    		switch(guibutton.id) {
	    	case 4:
	    		guiStage = 1;
	    		break;
	    	case 14:
	    		FurenikusEconomy.log(3, "[ATM] Open Withdraw GUI!");
	    		guiStage = 3;
	    		break;
	    	case 15:
	    		FurenikusEconomy.log(3, "[ATM] Open Balance GUI");
	    		guiStage = 4;
	    		break;
	    	case 16:
	    		break;
	    	case 17:
	    		FurenikusEconomy.log(3, "[ATM] Open Change Pin GUI");
	    		guiStage = 8;
	    		break;
	    	case 21:
	    		FurenikusEconomy.log(3, "[ATM] Eject Card");
	    		guiStage = 1;
	    	}
    	}
    	
    	if (guiStage == 3) {            
    		switch(guibutton.id) {
	    	case 4:
	    		guiStage = 1;
	    		break;
    		case 16:
    			FurenikusEconomy.log(3, "[ATM] Withdraw $10");
	    		withdrawAmount = 10;
	    		withdrawFunds(withdrawAmount);
	    		break;
	    	case 17:
	    		FurenikusEconomy.log(3, "[ATM] Withdraw $20");
	    		withdrawAmount = 20;
	    		withdrawFunds(withdrawAmount);
	    		break;
	    	case 18:
	    		FurenikusEconomy.log(3, "[ATM] Withdraw $50");
            	withdrawAmount = 50;
            	withdrawFunds(withdrawAmount);
	    		break;
	    	case 19:
	    		FurenikusEconomy.log(3, "[ATM] Withdraw $100");
	    		withdrawAmount = 100;
	    		withdrawFunds(withdrawAmount);
	    		break;
	    	case 20:
	    		FurenikusEconomy.log(3, "[ATM] Withdraw $250");
	    		withdrawAmount = 250;
	    		withdrawFunds(withdrawAmount);
	    		break;
	    	case 21:
	    		System.out.println("Withdraw custom amount");
	    		guiStage = 7;
	    		break;
	    	}
    	}
    	
    	if (guiStage == 4) {
    		switch(guibutton.id){
	    	case 4:
	    		guiStage = 1;
	    		break;
    		case 20:
    			guiStage = 2;
    			break;
    		}
    	}
    	
    	if (guiStage == 5) {
    		FurenikusEconomy.network.sendToServer(new SoundPacket(FurenikusEconomy.MODID + ":atmButton"));
    		switch(guibutton.id) {
    		case 4:
    			guiStage = 1;
    			break;
    		case 12:
    			FurenikusEconomy.log(3, "[ATM] Returned to menu.");
    			guiStage = 2;
    			break;
        	}
    	} 
    	
    	if (guiStage == 6) {
    		switch(guibutton.id) {
    		case 4:
    			guiStage = 1;
    			break;
    		case 17:
    			FurenikusEconomy.log(3, "[ATM] Withdraw Different Amount");
    			guiStage = 3;
    			break;
    		case 19:
    			FurenikusEconomy.log(3, "[ATM] Returned to menu.");
    			guiStage = 2;
    			break;
    		case 21:
    			FurenikusEconomy.log(3, "[ATM] Ejected card.");
    			guiStage = 1;
    			break;
        	}
    	}
    	
    	if (guiStage == 7) {
    		switch(guibutton.id) {
	    	case 9:
	    		withdrawCustom = withdrawCustom + "1";
	    		break;
	    	case 10:
	    		withdrawCustom = withdrawCustom + "2";
	    		break;
	    	case 11:
	    		withdrawCustom = withdrawCustom + "3";
	    		break;
	    	case 5:
	    		withdrawCustom = withdrawCustom + "4";
	    		break;
	    	case 6:
	    		withdrawCustom = withdrawCustom + "5";
	    		break;
	    	case 7:
	    		withdrawCustom = withdrawCustom + "6";
	    		break;
	    	case 1:
	    		withdrawCustom = withdrawCustom + "7";
	    		break;
			case 2:
				withdrawCustom = withdrawCustom + "8";
				break;
	    	case 3:
	    		withdrawCustom = withdrawCustom + "9";
	    		break;
	    	case 13:
	    		withdrawCustom = withdrawCustom + "0";
	    		break;
	    	case 8: //Clear
	    		withdrawCustom = "";
	    		break;
	    	case 12: //Confirm
	    		long customWithdrawFinal = econ.parseLong(withdrawCustom);
	            
	            withdrawAmount = customWithdrawFinal;
	            withdrawFunds(withdrawAmount);
	            
	    		if (initBalance >= withdrawAmount) {
	    			guiStage = 5;
	    		} else {
	    			guiStage = 6;
	    		}
	            withdrawCustom = "";
	    		break;
	    	case 4: //Cancel
	    		guiStage = 1;
	    		withdrawCustom = "";
	    		break;
    		case 20:
    			guiStage = 2;
    			withdrawCustom = "";
    			break;
    		}
    	}
    	
		if (guiStage >= 8) {
			switch(guibutton.id) {
	    	case 4: //Cancel
	    		guiStage = 1;
	    		break;
    		case 16:
    			guiStage = 2;
    			break;
			}
		}
		

		switch(guibutton.id) {
		case 1:
			FurenikusEconomy.log(3, "[ATM] You pressed 7");
			break;
		case 2:
			FurenikusEconomy.log(3, "[ATM] You pressed 8");
			break;
		case 3:
			FurenikusEconomy.log(3, "[ATM] You pressed 9");
			break;
		case 4:
			FurenikusEconomy.log(3, "[ATM] You pressed Cancel");
			break;
		case 5:
			FurenikusEconomy.log(3, "[ATM] You pressed 4");
			break;
		case 6:
			FurenikusEconomy.log(3, "[ATM] You pressed 5");
			break;
		case 7:
			FurenikusEconomy.log(3, "[ATM] You pressed 6");
			break;
		case 8:
			FurenikusEconomy.log(3, "[ATM] You pressed Clear");
			break;
		case 9:
			FurenikusEconomy.log(3, "[ATM] You pressed 1");
			break;
		case 10:
			FurenikusEconomy.log(3, "[ATM] You pressed 2");
			break;
		case 11:
			FurenikusEconomy.log(3, "[ATM] You pressed 3");
			break;
		case 12:
			FurenikusEconomy.log(3, "[ATM] You pressed Confirm");
			break;
		case 13:
			FurenikusEconomy.log(3, "[ATM] You pressed 0");
			break;
		case 14:
			FurenikusEconomy.log(3, "[ATM] You pressed the Top-Left ATM Button");
			break;
		case 15:
			FurenikusEconomy.log(3, "[ATM] You pressed the Top-Right ATM Button");
			break;
		case 16:
			FurenikusEconomy.log(3, "[ATM] You pressed the Mid-Upper-Left ATM Button");
			break;
		case 17:
			FurenikusEconomy.log(3, "[ATM] You pressed the Mid-Upper-Right ATM Button");
			break;
		case 18:
			FurenikusEconomy.log(3, "[ATM] You pressed the Mid-Lower-Left ATM Button");
			break;
		case 19:
			FurenikusEconomy.log(3, "[ATM] You pressed the Mid-Lower-Right ATM Button");
			break;
		case 20:
			FurenikusEconomy.log(3, "[ATM] You pressed the Bottom-Left ATM Button");
			break;
		case 21:
			FurenikusEconomy.log(3, "[ATM] You pressed the Bottom-Right ATM Button");
			break;
		}
    }
    
    @Override
    public void updateScreen() {
    	/*button0.visible = true;
    	button1.visible = true;
    	button2.visible = true;
    	button3.visible = true;
    	button4.visible = true;
    	button5.visible = true;
    	button6.visible = true;
    	button7.visible = true;
    	button8.visible = true;
    	button9.visible = true;*/
    }
    
    private void withdrawFunds(long amt) {
		if (initBalance >= withdrawAmount) {
			guiStage = 5;
			FurenikusEconomy.network.sendToServer(new ATMWithdrawPacket(amt));
		} else {
			guiStage = 6;
		}
    }
    
    private void isPinLongEnough (String pinCode) {
    	if (pinCode.length() == 5)
    		this.authenticatePin(pinCode);
    }
        
    private void authenticatePin (String pinCode) {
		EntityPlayerSP player = Minecraft.getMinecraft().player;
    	if (pinCode.equals(DebitCardItem.checkCardPin(player) + "c")) {
    		FurenikusEconomy.log(3, "[ATM] Correct Pin!");
    		guiStage = 2;
    		enteredPin = "";
    		pinAttempt = 1;
    	} else if (pinAttempt == 1) {
    		enteredPin = "";
    		pinAttempt = 2;
    	} else if (pinAttempt == 2) {
    		enteredPin = "";
    		pinAttempt = 3;
    	} else if (pinAttempt == 3) {
    		enteredPin = "";
    		pinAttempt = 4;
    		
            this.mc.displayGuiScreen(null);
    	}
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public void drawCenteredString(FontRenderer fontRenderer, String text, int x, int y, int color) {
        fontRenderer.drawString(text, (x - fontRenderer.getStringWidth(text) / 2), y, color);
    }
    
    public void drawRightString(FontRenderer fontRenderer, String text, int x, int y, int color) {
        fontRenderer.drawString(text, (x - fontRenderer.getStringWidth(text)), y, color);
    }
}