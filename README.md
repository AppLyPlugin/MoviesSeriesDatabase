# MoviesSeriesDatabase

Movies Series Database is a native android application that utilizes TMDB api to display movies and series list. This application used:

Programming Language: Kotlin

Architecture: MVVM

Data Persistence: Room, Datastore Repository

Dependency Injection: Dagger Hilt

Data and View Binding

Image Loading: Coil

Multithreading and Asynchronous calls: Coroutines and Flow

Navigation Components and Arguments

Network Listener

Contextual Action Mode



Compose of 3 main tabs:

1.	Movies Tab

2.	Series Tab

3.	Watchlist Tab



Movies and Series Tab:

•	Initially gets result from TMDB api using Retrofit and Okhttp with Coroutines with filter year = 2023, order = chip_sort_popularity.desc to display the top popular movies/series of the year using Recycler View.

•	Display simple details about the movies/series including title, genre, release date and rating

•	Rating textcolor is based on the rating (neutral = grey, positive = green, negative = red) using Adapter

•	Filter Action button to apply filter to Api request. Filter Sort and Year from 2023-2010/All time.

•	Search Action Bar on the upper right to search specific items with year filter, sort filter is not applicable. 

•	Clicking an item will display detailed activity information of the item using Navigation with Navargs

•	Offline mode will display the latest result from cache using ROOM database and displaying items called from Flow.

•	Detects if internet connectivity is Lost or Regained with NetworkListerner.

•	Datastore Repository for saving the filter selection of the current session


![Api Display Response](https://github.com/AppLyPlugin/MoviesSeriesDatabase/assets/5298665/9b48f49d-716a-4f9f-9ff7-8031b4afb242)
![Filter](https://github.com/AppLyPlugin/MoviesSeriesDatabase/assets/5298665/d485461b-e028-4c5a-9389-ba650737be18)
![Search](https://github.com/AppLyPlugin/MoviesSeriesDatabase/assets/5298665/bc60e39e-0af6-4e5c-af39-8c1e09bdf1a6)

Watchlist Tab

•	Displays items added to watchlist table in Room Database.

•	Contextual Action Mode highlighting single/multiple items to select the items and be able to delete from watchlist table

•	Menu option to delete all items

•	Clicking an item will display the detailed information of the item the same with Movies and Series tabs.

![Watchlist](https://github.com/AppLyPlugin/MoviesSeriesDatabase/assets/5298665/7e560aaf-d585-4d23-8c0c-fed30d13abb6)
![Contextual Action](https://github.com/AppLyPlugin/MoviesSeriesDatabase/assets/5298665/02106437-06ec-4dfa-a62f-677d2f964dce)
![Menu Delete All](https://github.com/AppLyPlugin/MoviesSeriesDatabase/assets/5298665/19da39cc-4c65-4f36-a6a0-1b0b1913f81c)


Detailed Activity

•	Display more detailed information about the movies/series including a banner, original language and summary.

•	Displays the name of the movie/series in the menu bar.

•	Option(star icon) to add the specific movie/series to watchlist

![Details Activity](https://github.com/AppLyPlugin/MoviesSeriesDatabase/assets/5298665/b4d7c084-6abd-431f-8315-79155492b507)

