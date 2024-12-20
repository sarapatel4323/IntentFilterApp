# Intent Filter Assessment

An Android application that demonstrates the use of intent filters to handle specific URI schemes and web requests. The app contains two activities: **MapViewActivity** for viewing geographical coordinates and **Humber Browser** for handling specific web requests to Humber College's website.

## Features

### MapViewActivity
- **Purpose**: Receives geo URIs with coordinates and zoom level, displays latitude, longitude, and zoom in a `TextView`.
- **Intent Filter**: Configured to accept `ACTION_VIEW` intents with `geo` URI schemes, allowing it to handle URIs structured as:
  - `geo:latitude,longitude`
  - `geo:latitude,longitude?z=zoom`
- **Data Display**: Parses the URI and extracts latitude, longitude, and zoom level, displaying them in a formatted `TextView`.
- **Favorite Location Management**: Users can mark locations on the map as favorites, view them on the map, and remove them when desired.
  
#### Favorite Location Feature:
- **Marking Favorites**: Tap any location on the map to save it as a favorite. The favorite location is saved internally in the app's storage.
- **Viewing Favorites**: All favorite locations are shown as markers on the map each time the app loads or the map refreshes.
- **Removing Favorites**: To delete a favorite location, click on a marker to open a context menu and select "Remove Favorite."

#### Example URIs:
- `geo:47.6,-122.3`
- `geo:47.6,-122.3?z=11`

### Humber Browser
- **Purpose**: Acts as a web browser to handle HTTP/HTTPS URIs specifically for `www.humber.ca`.
- **Intent Filter**: Configured to accept intents with `http` or `https` schemes that begin with `www.humber.ca`.
- **Activity Layout**:
  - A `TextView` at the top displays **"Welcome to Humber"** in large, bold font as a header.
  - A `WebView` within a fragment loads and displays the requested `www.humber.ca` URI.

## Technologies Used

- **Java**: Core programming language for app development.
- **Android Intents and Intent Filters**: Manages interactions between the app and external requests.
- **WebView**: Displays web content within the app.
  

## Screenshots

### Map View Activity
![Map View Activity](/output_maplocation/1.png)
![Map View Activity](/output_maplocation/2.png)

### Humber Browser Activity
![Humber Browser Activity](/output_maplocation/3.png)


