package com.buaa.texaspoker.client.gui;

import javax.swing.*;
import javax.swing.text.*;

/**
 * 为{@link JTextPane}添加自动换行
 * @author CPunisher
 */
public class MessageTextPane extends JTextPane {

    private static class WrapLabelView extends LabelView {
        public WrapLabelView(Element elem) {
            super(elem);
        }

        @Override
        public float getMinimumSpan(int axis) {
            switch (axis) {
                case View.X_AXIS: return 0;
                case View.Y_AXIS: return super.getMinimumSpan(axis);
            }
            return 0;
        }
    }

    private static class WrapColumnFactory implements ViewFactory {
        @Override
        public View create(Element elem) {
            String kind = elem.getName();
            switch (kind) {
                case AbstractDocument.ContentElementName: return new WrapLabelView(elem);
                case AbstractDocument.ParagraphElementName: return new ParagraphView(elem);
                case AbstractDocument.SectionElementName: return new BoxView(elem, View.Y_AXIS);
                case StyleConstants.ComponentElementName: return new ComponentView(elem);
                case StyleConstants.IconElementName: return new IconView(elem);
            }
            return new LabelView(elem);
        }
    }

    private static class WrapEditorKit extends StyledEditorKit {
        private ViewFactory factory = new WrapColumnFactory();

        @Override
        public ViewFactory getViewFactory() {
            return factory;
        }
    }

    public MessageTextPane() {
        super();
        this.setEditorKit(new WrapEditorKit());
    }
}
