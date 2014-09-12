Exercise 2: Create streams
====================================

Show the plane image on the correct location on the screen by subscribing to the stream of positions of the plane.
See class be.cegeka.android.rx.presentation.MainFragment.

    1. Map the game to the plane.
    2. Flat map the stream of positions on the plane
    3. Subscribe to the stream of positions of the plane. Adjust the location of the plane image on each item emitted.

        ATTENTION!!! Subscribe on a background thread and observe on the main thread
        (HINT: use Schedulers and AndroidSchedulers)
