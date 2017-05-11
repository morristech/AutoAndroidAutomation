package testUtilities;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Known Flaky tests/issues:
 * 
 * 1. Any test that utilizes the menu.
 * Menu items text view all share the same ids.
 * And there's a weird situation where if you go from Menu screen # 1 to Menu Screen #2 to Menu Screen #3, 
 * there's a chance that items from screen #1 will be returned even though you are currently on screen #3.
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Flaky {}
