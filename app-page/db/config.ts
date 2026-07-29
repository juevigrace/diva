import { column, defineDb, defineTable } from 'astro:db';

const User = defineTable({
  columns: {
    userId: column.text({ primaryKey: true }),
    username: column.text(),
    email: column.text(),
    phoneNumber: column.text({ optional: true }),
    role: column.text(),
    createdAt: column.number(),
    updatedAt: column.number(),
    deletedAt: column.number({ optional: true }),
    cachedAt: column.number(),
  },
});

const UserState = defineTable({
  columns: {
    userId: column.text({ primaryKey: true }),
    verified: column.boolean(),
    status: column.text(),
    lastActiveAt: column.number(),
    updatedAt: column.number(),
    cachedAt: column.number(),
  },
});

const UserProfile = defineTable({
  columns: {
    userId: column.text({ primaryKey: true }),
    firstName: column.text({ optional: true }),
    lastName: column.text({ optional: true }),
    birthDate: column.number({ optional: true }),
    alias: column.text({ optional: true }),
    avatar: column.text({ optional: true }),
    bio: column.text({ optional: true }),
    cachedAt: column.number(),
  },
});

export default defineDb({
  tables: { User, UserState, UserProfile },
});
