package org.stackexchange.strategies;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public final class StackExchangePageStrategyUnitTest {

    // tests

    @Test
    public final void whenDecidingCorrectPage_thenNoExceptions() {
        new StackExchangePageStrategy().decidePageInternal(1);
    }

    @Test
    public final void givenPageIs1_whenDecidingCorrectPage_thenPageIs1() {
        final int decidedPage = new StackExchangePageStrategy().decidePageInternal(5);
        assertThat(decidedPage, equalTo(1));
    }

    @Test
    public final void givenPageIs2_whenDecidingCorrectPage_thenPageIs2() {
        final int decidedPage = new StackExchangePageStrategy().decidePageInternal(35);
        assertThat(decidedPage, equalTo(2));
    }

    @Test
    public final void givenPageIs3_whenDecidingCorrectPage_thenPageIs3() {
        final int decidedPage = new StackExchangePageStrategy().decidePageInternal(65);
        assertThat(decidedPage, equalTo(3));
    }

    @Test
    public final void givenPageIsOver3_whenDecidingCorrectPage_thenCorrect() {
        final int decidedPage = new StackExchangePageStrategy().decidePageInternal(155);
        assertThat(decidedPage, lessThan(9));
    }

}
