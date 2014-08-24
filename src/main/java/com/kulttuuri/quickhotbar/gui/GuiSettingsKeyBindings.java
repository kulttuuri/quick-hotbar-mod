package com.kulttuuri.quickhotbar.gui;

import com.kulttuuri.quickhotbar.QuickHotbarMod;
import com.kulttuuri.quickhotbar.gui.components.ToolTipHelper;
import com.kulttuuri.quickhotbar.settings.SettingsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

/**
 * GUI for information screen.
 * @author Aleksi Postari / Kulttuuri
 */
public class GuiSettingsKeyBindings extends GuiSettingsBase
{
    private ArrayList<GuiTextField> textfieldBindings = new ArrayList<GuiTextField>();
    private static boolean isEditingKeybinding = false;

    private String errorText = "";
    private int errorX = 0;
    private int errorY = 0;
    private long errorSetTime = 0;

    private static final int TEXTFIELD_MODIFIER = 0;
    private static final int TEXTFIELD_SWITCH_MODE = 1;
    private static final int TEXTFIELD_OPEN_MENU = 2;
    private static final int TEXTFIELD_SCROLL_UP = 3;
    private static final int TEXTFIELD_SCROLL_DOWN = 4;

    private ArrayList<TextFieldPreferencesContainer> textfieldPreferences = new ArrayList<TextFieldPreferencesContainer>();

    public GuiSettingsKeyBindings()
    {
        currentGuiScreen = this;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        Keyboard.enableRepeatEvents(true);

        textfieldBindings.clear();
        textfieldPreferences.clear();

        int fieldWidth = 100;
        int fieldHeight = 15;

        textfieldBindings.add(TEXTFIELD_MODIFIER, new GuiTextField(Minecraft.getMinecraft().fontRenderer, width / 2 - 159, height / 2 - 34, fieldWidth, fieldHeight));
        textfieldBindings.add(TEXTFIELD_SWITCH_MODE, new GuiTextField(Minecraft.getMinecraft().fontRenderer, width / 2 - 159, height / 2 + 15, fieldWidth, fieldHeight));
        textfieldBindings.add(TEXTFIELD_OPEN_MENU, new GuiTextField(Minecraft.getMinecraft().fontRenderer, width / 2 - 159, height / 2 + 45, fieldWidth, fieldHeight));

        textfieldBindings.add(TEXTFIELD_SCROLL_UP, new GuiTextField(Minecraft.getMinecraft().fontRenderer, width / 2 - 30, height / 2 + 15, fieldWidth, fieldHeight));
        textfieldBindings.add(TEXTFIELD_SCROLL_DOWN, new GuiTextField(Minecraft.getMinecraft().fontRenderer, width / 2 - 30, height / 2 + 45, fieldWidth, fieldHeight));

        // Hacky 1.6.4 GuiTextField preferences...
        textfieldPreferences.add(TEXTFIELD_MODIFIER, new TextFieldPreferencesContainer(width / 2 - 159, height / 2 - 34, fieldWidth, fieldHeight));
        textfieldPreferences.add(TEXTFIELD_SWITCH_MODE, new TextFieldPreferencesContainer(width / 2 - 159, height / 2 + 15, fieldWidth, fieldHeight));
        textfieldPreferences.add(TEXTFIELD_OPEN_MENU, new TextFieldPreferencesContainer(width / 2 - 159, height / 2 + 45, fieldWidth, fieldHeight));
        textfieldPreferences.add(TEXTFIELD_SCROLL_UP, new TextFieldPreferencesContainer(width / 2 - 30, height / 2 + 15, fieldWidth, fieldHeight));
        textfieldPreferences.add(TEXTFIELD_SCROLL_DOWN, new TextFieldPreferencesContainer(width / 2 - 30, height / 2 + 45, fieldWidth, fieldHeight));

        updateValues();
    }

    private void updateValues()
    {
        SettingsClient set = QuickHotbarMod.clientSettings;

        textfieldBindings.get(TEXTFIELD_MODIFIER).setText(Keyboard.getKeyName(set.SCROLLING_KEY));
        textfieldBindings.get(TEXTFIELD_SWITCH_MODE).setText(Keyboard.getKeyName(set.SCROLLING_KEY_SWITCH_MODE));
        textfieldBindings.get(TEXTFIELD_OPEN_MENU).setText(Keyboard.getKeyName(set.KEY_OPEN_MOD_SETTINGS_MENU));
        textfieldBindings.get(TEXTFIELD_SCROLL_UP).setText(Keyboard.getKeyName(set.SCROLLING_KEY_UP));
        textfieldBindings.get(TEXTFIELD_SCROLL_DOWN).setText(Keyboard.getKeyName(set.SCROLLING_KEY_DOWN));
    }

    @Override
    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);

        updateValues();

        for (GuiTextField textfield : textfieldBindings)
        {
            textfield.mouseClicked(i, j, k);
            if (textfield.isFocused())
            {
                textfield.setText("");
                isEditingKeybinding = true;
            }
        }
    }

    @Override
    protected void keyTyped(char c, int i)
    {
        boolean textfieldHasFocus = false;

        for (int m = 0; m < textfieldBindings.size(); m++)
        {
            GuiTextField textfield = textfieldBindings.get(m);
            if (textfield.isFocused()) textfieldHasFocus = true;
            textfield.textboxKeyTyped(c, i);

            if (textfield.isFocused() && i == Keyboard.KEY_ESCAPE)
            {
                textfield.setFocused(false);
                updateValues();
            }
            else if (textfield.isFocused() && i != Keyboard.KEY_NONE)
            {
                if (!setKeyBinding(m, i))
                {
                    TextFieldPreferencesContainer prefs = textfieldPreferences.get(m);
                    setErrorTooltip("Error: That keybinding is already reserved.", prefs.xPos - (prefs.width / 6), prefs.yPos);
                }
                textfield.setFocused(false);
                updateValues();
            }
        }

        if (i == Keyboard.KEY_ESCAPE && !textfieldHasFocus)
        {
            closeGui();
        }
    }

    private boolean setKeyBinding(int keybinding, int key)
    {
        SettingsClient set = QuickHotbarMod.clientSettings;

        if (isKeybindingReserved(key)) return false;

        if (keybinding == TEXTFIELD_MODIFIER)
        {
            set.SCROLLING_KEY = key;
        }
        else if (keybinding == TEXTFIELD_SWITCH_MODE)
        {
            set.SCROLLING_KEY_SWITCH_MODE = key;
        }
        else if (keybinding == TEXTFIELD_OPEN_MENU)
        {
            set.KEY_OPEN_MOD_SETTINGS_MENU = key;
        }
        else if (keybinding == TEXTFIELD_SCROLL_UP)
        {
            set.SCROLLING_KEY_UP = key;
        }
        else if (keybinding == TEXTFIELD_SCROLL_DOWN)
        {
            set.SCROLLING_KEY_DOWN = key;
        }

        set.saveSettings();
        return true;
    }

    private boolean isKeybindingReserved(int key)
    {
        SettingsClient set = QuickHotbarMod.clientSettings;

        if (key == set.SCROLLING_KEY ||
                key == set.SCROLLING_KEY_SWITCH_MODE ||
                key == set.KEY_OPEN_MOD_SETTINGS_MENU ||
                key == set.SCROLLING_KEY_UP ||
                key == set.SCROLLING_KEY_DOWN)
        {
            return true;
        }

        return false;
    }

    private void setErrorTooltip(String text, int x, int y)
    {
        errorSetTime = Minecraft.getMinecraft().getSystemTime();
        errorText = text;
        errorX = x;
        errorY = y;
    }

    @Override public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);

        fontRenderer.drawString("Modifier Key:", width / 2 - 160, height / 2 - 45, 0x000000);

        fontRenderer.drawString("Switch Mode:", width / 2 - 160, height / 2 + 5, 0x000000);
        fontRenderer.drawString("Open Inventory:", width / 2 - 160, height / 2 + 35, 0x000000);

        fontRenderer.drawString("Manual Scroll Up:", width / 2 - 30, height / 2 + 5, 0x000000);
        fontRenderer.drawString("Manual Scroll Down:", width / 2 - 30, height / 2 + 35, 0x000000);

        boolean updateCursor = Minecraft.getMinecraft().getSystemTime() % 3 == 0 ? true : false;
        for (int m = 0; m < textfieldBindings.size(); m++)
        {
            GuiTextField textfield = textfieldBindings.get(m);
            if (textfield != null) textfield.drawTextBox();
            if (updateCursor) textfield.updateCursorCounter();
            if (textfield.isFocused())
            {
                TextFieldPreferencesContainer prefs = textfieldPreferences.get(m);
                ToolTipHelper.setTooltip("Type a key...<br>Press esc or click<br>anywhere to cancel.", prefs.xPos - (prefs.width / 6) + 1, prefs.yPos);
                errorSetTime = 0;
            }
        }

        if (errorSetTime != 0 && Minecraft.getMinecraft().getSystemTime() - errorSetTime < 2400)
        {
            ToolTipHelper.setTooltip(errorText, errorX, errorY);
        }
        else
        {
            errorSetTime = 0;
        }

        ToolTipHelper.renderToolTipAndClear(this);
    }

    /**
     * Because in 1.6.4 GuiTextField.xPos etc. has private access we have this to access these settings
     * after instance has been created...
     */
    private class TextFieldPreferencesContainer
    {
        public int xPos;
        public int yPos;
        public int width;
        public int height;

        public TextFieldPreferencesContainer(int xPos, int yPos, int width, int height)
        {
            this.xPos = xPos;
            this.yPos = yPos;
            this.width = width;
            this.height = height;
        }
    }
}