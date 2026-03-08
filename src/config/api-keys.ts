/**
 * Fixed API keys — loaded from .env.
 * BPM uses the Deezer public API (no key required).
 * To update a key, change it in .env and restart the server.
 */
export const GROQ_API_KEY = process.env['GROQ_API_KEY'] ?? '';
