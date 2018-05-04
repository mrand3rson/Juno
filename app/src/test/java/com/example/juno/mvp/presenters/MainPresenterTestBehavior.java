package com.example.juno.mvp.presenters;

import com.example.juno.mvp.models.GithubApiService;
import com.example.juno.mvp.models.Repository;
import com.example.juno.mvp.views.MainView;
import com.greghaskins.spectrum.Spectrum;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;

import static com.greghaskins.spectrum.dsl.specification.Specification.describe;
import static com.greghaskins.spectrum.dsl.specification.Specification.it;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Andrei on 03.05.2018.
 */
@RunWith(Spectrum.class)
public class MainPresenterTestBehavior {

    private MainView view = mock(MainView.class);
    private GithubApiService apiService = mock(GithubApiService.class);
    private TestScheduler testScheduler = new TestScheduler();
    private MainPresenter presenter = new MainPresenter(view, apiService, testScheduler, testScheduler);

    {
        describe("The Presenter", () -> {
            it("should get empty observable when calling api function", ()-> {
                Mockito.when(apiService.getJunoRepositories()).thenReturn(Observable.just(Collections.emptyList()));
            });

            it("should call show result on getting List<Repository>", ()-> {
                presenter.getRepositories();
                testScheduler.triggerActions();
                verify(view, VerificationModeFactory.times(0)).showError(any());
                verify(view).showResult(any());
            });

            it("should not have null response", ()-> {
                presenter.getRepositories();
                testScheduler.triggerActions();
                verify(view, VerificationModeFactory.times(0)).showResult(null);
            });

            it("'s filter should work properly", ()-> {
                List<Repository> list = new ArrayList<>(3);
                list.add(new Repository(0, "abcdefghijklmnopqrstuvwxyz"));
                list.add(new Repository(1, "abcdefghij"));
                list.add(new Repository(2, "abcd"));

                presenter.data = list;
                assertTrue(presenter.filterRecyclerItems("").size() == 3);
                assertTrue(presenter.filterRecyclerItems("abcd").size() == 3);
                assertTrue(presenter.filterRecyclerItems("abcdef").size() == 2);
                assertTrue(presenter.filterRecyclerItems("op").size() == 1);
                assertTrue(presenter.filterRecyclerItems("po").isEmpty());
            });
        });
    }
}
