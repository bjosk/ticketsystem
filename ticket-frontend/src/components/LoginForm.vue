<template>
  <form class="p-4 border rounded bg-white shadow-sm w-50">
    <h2 class="mb-3 text-center">Login</h2>
    <div class="mb-3">
      <label for="username" class="form-label">Username</label>
      <input
        type="text"
        class="form-control"
        id="username"
        placeholder="Enter username"
      />
    </div>

    <div class="mb-3">
      <label for="password" class="form-label">Password</label>
      <input
        type="password"
        class="form-control"
        id="password"
        placeholder="Enter password"
      />
    </div>

    <button type="submit" class="btn btn-primary w-100">Login</button>
  </form>
</template>
