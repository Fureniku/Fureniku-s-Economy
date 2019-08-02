package com.silvaniastudios.econ.core.client.gui.atm;

import org.lwjgl.opengl.GL11;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.core.EconConfig;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.core.items.DebitCardItem;
import com.silvaniastudios.econ.network.ATMWithdrawPacket;
import com.silvaniastudios.econ.network.ServerBalancePacket;
import com.silvaniastudios.econ.network.SoundPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiATM extends GuiScreen {

    private static final ResourceLocation texture = new ResourceLocation("flenixcities", "textures/gui/atm.png");
    public EconUtils econ = new EconUtils();

    protected int xSize = 232;
    protected int ySize = 242;
    
    String enteredPin = "";
    int pinAttempt = 1;
    int guiStage = 1;
    String withdrawCustom = "";
    long withdrawAmount;
    long initBalance;
    
    private GuiButton button0;
    private GuiButton button1;
    private GuiButton button2;
    private GuiButton button3;
    
    //GUI Stage changes the current active screen. It enables different features on the buttons, and changes the text.
    //Stage 1 asks for pin and waits for it to be entered.
    //Stage 2 is an options menu, asking for your next move.
    //Stage 3 is the Withdraw screen, the buttons will give money and remove from your NBT
    //Stage 4 is the Balance screen, and shows your balance.
    //Stage 5 is the withdraw confirm screen.
    //Stage 6 is the insufficient funds error message.
    //Stage 7 is the Withdraw X Amount screen.
    //Stage 8+ informs of an invalid selection
    
    @Override
    public void initGui() {
    	System.out.println("initGui");
    	buttonList.clear();
    	int guiLeft = width / 2;
    	int guiTop = height / 2;
    	FurenikusEconomy.network.sendToServer(new SoundPacket("flenixcities:cardInsert"));
    	buttonList.add(new ATMButton(1, guiLeft + 21, guiTop + 109, 24, 15, "7")); // 7
    	buttonList.add(new ATMButton(2, guiLeft + 53, guiTop + 109, 24, 15, "8")); // 8
    	buttonList.add(new ATMButton(3, guiLeft + 85, guiTop + 109, 24, 15, "9")); // 9
    	buttonList.add(new RedButton(4, guiLeft + 121, guiTop + 109, 24, 15, "")); // Cancel
    	buttonList.add(new ATMButton(5, guiLeft + 21, guiTop + 133, 24, 15, "4")); // 4
    	buttonList.add(new ATMButton(6, guiLeft + 53, guiTop + 133, 24, 15, "5")); // 5
    	buttonList.add(new ATMButton(7, guiLeft + 85, guiTop + 133, 24, 15, "6")); // 6
    	buttonList.add(new YellowButton(8, guiLeft + 121, guiTop + 133, 24, 15, "")); // Clear
    	buttonList.add(button1 = new ATMButton(9, guiLeft + 21, guiTop + 157, 24, 15, "1")); // 1
    	buttonList.add(button2 = new ATMButton(10, guiLeft + 53, guiTop + 157, 24, 15, "2")); // 2
    	buttonList.add(button3 = new ATMButton(11, guiLeft + 85, guiTop + 157, 24, 15, "3")); // 3
    	buttonList.add(new GreenButton(12, guiLeft + 121, guiTop + 157, 24, 15, "")); // Confirm
    	buttonList.add(button0 = new ATMButton(13, guiLeft + 53, guiTop + 181, 24, 15, "0")); // 0
    	
    	buttonList.add(new ATMButtonLeft(14, guiLeft - 21, guiTop - 7, 24, 15, "")); //Top-Left
    	buttonList.add(new ATMButtonLeft(16, guiLeft - 21, guiTop + 20, 24, 15, "")); //Mid-Upper Left
    	buttonList.add(new ATMButtonLeft(18, guiLeft - 21, guiTop + 47, 24, 15, "")); //Mid-Lower Left
    	buttonList.add(new ATMButtonLeft(20, guiLeft - 21, guiTop + 74, 24, 15, "")); //Bottom Left
    	
    	buttonList.add(new ATMButtonRight(15, guiLeft + 173, guiTop - 7, 24, 15, "")); //Top-Right
    	buttonList.add(new ATMButtonRight(17, guiLeft + 173, guiTop + 20, 24, 15, "")); //Mid-Upper Right
    	buttonList.add(new ATMButtonRight(19, guiLeft + 173, guiTop + 47, 24, 15, "")); //Mid-Lower Right
    	buttonList.add(new ATMButtonRight(21, guiLeft + 173, guiTop + 74, 24, 15, "")); //Bottom Right
    }
    
    public void actionPerformed(GuiButton guibutton) {
    	initBalance = econ.parseLong(ServerBalancePacket.balanceAmount);
    	FurenikusEconomy.log(2, "Balance available in GUI! Balance: $" + initBalance);
    	//Plays the sound, and while it's at it also updates client balance.
    	//Updating the balance on every button press means withdrawls etc will update the balance, and makes it effectively bulletproof
    	//against latency issues.
    	FurenikusEconomy.network.sendToServer(new SoundPacket("flenixcities:atmButton"));
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
    		FurenikusEconomy.network.sendToServer(new SoundPacket("flenixcities:atmButton"));
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
    private int tick = 0;
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int x = (width-xSize) / 2;
        int y = (height-ySize) / 2;
        int right = 28;
        int up = -88;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        
        button0.drawButton(mc, mouseX, mouseY, partialTicks);
        button1.drawButton(mc, mouseX, mouseY, partialTicks);
        button2.drawButton(mc, mouseX, mouseY, partialTicks);
        button3.drawButton(mc, mouseX, mouseY, partialTicks);
    	
    	fontRenderer.drawString("ATM", x-21, x-30, 0x404040);
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
	    	fontRenderer.drawString("ATM", x-21+right, x-30+up, 0x404040);
	    	fontRenderer.drawString("Welcome!", x+68+right, x-2+up, 0x007F0E);
	    	fontRenderer.drawString("Please enter your PIN,", x+32+right, x+8+up, 0x007F0E);
	    	fontRenderer.drawString("followed by 'Confirm'", x+37+right, x+18+up, 0x007F0E);
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
    		fontRenderer.drawString(maskedPin, 78, 48, 0x007F0E);
	    	if (pinAttempt == 4) {
		    	fontRenderer.drawString("Attempt 3 of 3.", 52, 68, 0x007F0E);
	    		fontRenderer.drawString("Card declined!", 53, 78, 0x7F0000);
	    	} else {
	    		fontRenderer.drawString("Attempt " + pinAttempt + " of 3.", x+52, x+68, 0x007F0E);
	    	}
    	}
    	if (guiStage == 2) {
        	fontRenderer.drawString("ATM", -21, -30, 0x404040);
        	fontRenderer.drawString("Withdraw", 12, -3, 0x007F0E);
        	fontRenderer.drawString("Balance", 126, -3, 0x007F0E);
        	fontRenderer.drawString("Eject Card", 109, 78, 0x007F0E);
    	}
    	if (guiStage == 3) {
        	fontRenderer.drawString("ATM", -21, -30, 0x404040);
    		fontRenderer.drawString("Please select the amount", 26, -12, 0xFFD800);
	    	fontRenderer.drawString("you wish to withdraw.", 37, -2, 0xFFD800);
        	fontRenderer.drawString("10", 12, 24, 0x007F0E);
        	fontRenderer.drawString("50", 12, 51, 0x007F0E);
        	fontRenderer.drawString("250", 12, 78, 0x007F0E);
        	fontRenderer.drawString("20", 153, 24, 0x007F0E);
        	fontRenderer.drawString("100", 147, 51, 0x007F0E);
        	fontRenderer.drawString("Input Amount", 100, 78, 0x007F0E);
    	}
    	if (guiStage == 4) {
    		fontRenderer.drawString("ATM", -21, -30, 0x404040);
    		fontRenderer.drawString("Your current balance is: ", 26, 8, 0x007F0E);
    		fontRenderer.drawString(bal, 26, 18, 0x007F0E);
        	fontRenderer.drawString("Back", 12, 78, 0x007F0E);
    	}
    	if (guiStage == 5) {
        	fontRenderer.drawString("ATM", -21, -30, 0x404040);
    		fontRenderer.drawString("Withdrawl Successful!", 35, 8, 0x007F0E);
    		fontRenderer.drawString("You have withdrawn", 39, 18, 0x007F0E);
    		fontRenderer.drawString(EconConfig.currencySign + econ.formatBalance(withdrawAmount), 50, 28, 0x007F0E);
    		fontRenderer.drawString("Press Confirm to continue.", 22, 58, 0x007F0E);

    	}
    	if (guiStage == 6) {
        	fontRenderer.drawString("ATM", -21, -30, 0x404040);
    		fontRenderer.drawString("Insufficient Funds!", 41, -2, 0x7F0000);
    		fontRenderer.drawString("$" + shortAmt + " more needed!", 43, 8, 0x7F0000);
    		fontRenderer.drawString("Withdraw Less", 97, 24, 0x007F0E);
    		fontRenderer.drawString("Return to Menu", 90, 51, 0x007F0E);
        	fontRenderer.drawString("Eject Card", 109, 78, 0x007F0E);
    	}
    	if (guiStage == 7) {
        	fontRenderer.drawString("ATM", -21, -30, 0x404040);
    		fontRenderer.drawString("Please enter the amount", 26, -12, 0xFFD800);
    		fontRenderer.drawString("you wish to withdraw:", 37, -2, 0xFFD800);
    		fontRenderer.drawString("$" + withdrawCustom, 72, 24, 0x007F0E);
        	fontRenderer.drawString("Back", 12, 78, 0x007F0E);
    	}
    	if (guiStage >= 8) {
        	fontRenderer.drawString("ATM", -21, -30, 0x404040);
    		fontRenderer.drawString("   Invalid Selection    ", 26, -12, 0xFFD800);
        	fontRenderer.drawString("Main Menu", 12, 24, 0x007F0E);
    	}
    }
    
    @Override
    public void onGuiClosed() {
    	System.out.println("Closed GUI!");
    }
}