package com.divyesh.framework.pages;

import org.openqa.selenium.WebDriver;

/**
 * Find Transactions uses a tabbed, multi-criteria search form. Only navigation and page-load
 * behaviour are automated here; the individual search tabs are a candidate for future extension.
 */
public class FindTransactionsPage extends BasePage {

    public FindTransactionsPage(WebDriver driver) {
        super(driver);
    }
}
