# Library Management System - Frontend

A modern, responsive web interface for the Library Management System built with HTML, CSS, and JavaScript.

## Features

- 📊 **Dashboard** - Overview with statistics and recent activity
- 📚 **Book Management** - Add, edit, delete books with quantity tracking
- 👥 **Member Management** - Add, edit, delete library members
- 🔄 **Lending Management** - Lend and return books with history tracking
- 🔍 **Search & Filter** - Real-time search across books and members
- 📱 **Responsive Design** - Works on desktop, tablet, and mobile
- 🎨 **Modern UI** - Beautiful gradients and smooth animations

## Setup

1. **Ensure the Quarkus backend is running** on `http://localhost:8080`

2. **Open the frontend** in your browser:
   - Double-click `index.html` to open directly
   - Or use a local server (recommended):
     ```bash
     # Using Python 3
     python -m http.server 8000
     
     # Using Node.js (if you have http-server installed)
     npx http-server -p 8000
     
     # Using PHP
     php -S localhost:8000
     ```

3. **Access the application** at `http://localhost:8000`

## Usage

### Dashboard
- View total books, members, active lendings, and available books
- See recent lending activity

### Books
- **Add Book**: Click "Add Book" button
- **Search**: Use the search bar to filter books
- **Edit/Delete**: Use action buttons in the table
- **Quantity**: Shows available copies (quantity increases when adding same book)

### Members
- **Add Member**: Click "Add Member" button
- **Search**: Use the search bar to filter members
- **Edit/Delete**: Use action buttons in the table

### Lending
- **Lend Book**: Click "Lend Book" button, select book and member
- **Return Book**: Click "Return" button for active lendings
- **View History**: Switch between "Active Lendings" and "Lending History" tabs

## API Integration

The frontend communicates with the Quarkus backend API:

- **Books**: `/books` (GET, POST, PUT, DELETE)
- **Members**: `/members` (GET, POST, PUT, DELETE)
- **Lending**: `/lending` (POST), `/lending/returns/{id}` (POST), `/lending/history` (GET)

## Browser Compatibility

- Chrome 60+
- Firefox 55+
- Safari 12+
- Edge 79+

## File Structure

```
frontend/
├── index.html          # Main HTML file
├── css/
│   └── style.css       # All styles and responsive design
├── js/
│   └── app.js          # JavaScript functionality
└── README.md           # This file
```

## Features in Detail

### Quantity-Based Book Management
- Adding the same book increases quantity instead of creating duplicates
- Lending decreases quantity, returning increases it
- Books with quantity 0 are marked as unavailable

### Real-time Updates
- Dashboard statistics update automatically
- Tables refresh after operations
- Toast notifications for user feedback

### Responsive Design
- Mobile-first approach
- Hamburger menu for mobile navigation
- Optimized layouts for all screen sizes

### User Experience
- Loading states during API calls
- Empty states with helpful messages
- Confirmation dialogs for destructive actions
- Keyboard shortcuts (ESC to close modals)

## Troubleshooting

### CORS Issues
If you see CORS errors, ensure:
1. The Quarkus backend is running on `http://localhost:8080`
2. CORS is properly configured in `application.properties`

### API Connection Issues
- Check that the backend is running
- Verify the API URL in `js/app.js` (line 2)
- Check browser console for error messages

### Styling Issues
- Ensure all CSS files are loaded
- Check that Font Awesome icons are loading
- Verify Google Fonts connection

## Customization

### Colors
Edit the CSS variables in `css/style.css` to change the color scheme:
- Primary gradient: `#667eea` to `#764ba2`
- Success: `#10b981`
- Danger: `#ef4444`

### API Endpoint
Change the API base URL in `js/app.js`:
```javascript
const API_BASE_URL = 'http://your-api-url:port';
```

## License

This frontend is part of the Library Management System project.
