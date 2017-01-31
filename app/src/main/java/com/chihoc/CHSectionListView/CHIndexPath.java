package com.chihoc.CHSectionListView;

public class CHIndexPath {

    protected int mSection;
    protected int mRow;

    public int getSection() {
        return mSection;
    }

    public void setSection(int mSection) {
        this.mSection = mSection;
    }

    public int getRow() {
        return mRow;
    }

    public void setRow(int mRow) {
        this.mRow = mRow;
    }

    public CHIndexPath(int section, int row) {
        mSection = section;
        mRow = row;
    }

    public CHIndexPath() {
        mSection = 0;
        mRow = 0;
    }
}
